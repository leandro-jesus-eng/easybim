package br.com.cjt.easybim.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cjt.easybim.controller.exception.ResourceNotFoundException;
import br.com.cjt.easybim.data.Person;
import br.com.cjt.easybim.repository.TabelaCustosIndicesRepository;
import br.com.cjt.easybim.sinapi.ConvertXlsToObj;
import br.com.cjt.easybim.sinapi.data.Composicao;
import br.com.cjt.easybim.sinapi.data.Insumo;
import br.com.cjt.easybim.sinapi.data.ItemComposicao;
import br.com.cjt.easybim.sinapi.data.NomeTabelas;
import br.com.cjt.easybim.sinapi.data.TabelaCustosIndices;

//TODO criar composições próprias baseadas no SINAPI. guardar de onde saiu, histórico
//TODO criar insumos próprios baseadas no SINAPI. guardar de onde saiu, histórico

//TODO Insumo, adicionar MACRO Classe - importante para fazer o histograma
//TODO Insumo, coeficiente desconhecido q está na família de insumos serve para calcular o valor baseado em um item de agregação
//TODO Insumo, verificar onde estão os insumos e composições que estão sem custo ... verificar o arquivo excel catálogo de referenciais

//TODO Orçamento, precisa confrontar o atribuído a São Paulo, adicionar o percentual atribuído a São Paulo

//TODO testar @Transactional
//TODO Service, evitar o uso de objetos da classe para não haver problemas de alteração de estado de objetos por requisições concorrentes 

@Service
@Transactional
public class TabelaCustosIndicesService {

	public static String COULD_NOT_FIND = "Could not find = ";

	private Set<Object> savedComposicao = null;

	@Autowired
	private TabelaCustosIndicesRepository tabelaCustosIndicesRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * Retorna a tabela de custos e indices, incluindo suas composições e insumos
	 * 
	 * @param nameTable  Pode ser (SINAPI, SICRO, PROPRIA)
	 * @param dataPreco  No formato 'mês/ano'. Ex.: dezembro/2022
	 * @param localidade
	 * @return
	 */
	public List<TabelaCustosIndices> find(String nameTable, String localidade, String dataPreco) {
		if (nameTable == null || dataPreco == null || localidade == null)
			throw new br.com.cjt.easybim.controller.exception.IllegalArgumentException(
					"Parameters: (nameTable, dataPreco, localidade) cannot be null");

		// orElseThrow não está mudando o fluxo de execução do código
		List<TabelaCustosIndices> ltci = tabelaCustosIndicesRepository.find(nameTable, localidade, dataPreco)
				.orElseThrow(() -> new ResourceNotFoundException(COULD_NOT_FIND + "nameTable=" + nameTable
						+ " localidade=" + localidade + " dataPreco=" + dataPreco));

		return ltci;
	}

	/**
	 * 
	 * @return
	 */
	public List<NomeTabelas> findNomeTabelas() {
		//return Stream.of(NomeTabelas.values()).map(NomeTabelas::name).collect(Collectors.toList());
		return NomeTabelas.values();
	}

	/**
	 * Retorna todas as datas de referências de uma dada tabela E.: janeiro/2021,
	 * abril/2021, julho/2021
	 * 
	 * @param nameTable nameTable Pode ser (SINAPI, SICRO, PROPRIA)
	 * @return uma lista com as data de preco. Ex. janeiro/2021, abril/2021,
	 *         julho/2021
	 */
	public List<String> findDataPrecos(String nameTable) {

		List<String> dataPrecosList = new ArrayList<String>();
		mongoTemplate.getCollection("tabelaCustosIndices").distinct("dataPreco", String.class).iterator()
				.forEachRemaining(dataPrecosList::add);

		if (dataPrecosList.size() == 0)
			throw new ResourceNotFoundException(COULD_NOT_FIND + nameTable);

		return dataPrecosList;
	}

	/**
	 * 
	 * @param nameTable
	 * @return
	 */
	public List<String> findLocalidades(String nameTable) {

		List<String> localidadesList = new ArrayList<String>();
		mongoTemplate.getCollection("tabelaCustosIndices").distinct("localidade", String.class).iterator()
				.forEachRemaining(localidadesList::add);

		if (localidadesList.size() == 0)
			throw new ResourceNotFoundException(COULD_NOT_FIND + nameTable);

		return localidadesList;
	}

	/**
	 * Salva a tabela de custos e índices
	 * 
	 * @param tabelaCustosIndices
	 * @return
	 */
	public TabelaCustosIndices save(TabelaCustosIndices tabelaCustosIndices) {
		return tabelaCustosIndicesRepository.save(tabelaCustosIndices);
	}

	/**
	 * Salva recursivamente os dados da Tabela e de todos as composições e insumos
	 * 
	 * @param inputStream
	 * @return
	 */
	public TabelaCustosIndices save(InputStream inputStream) {

		System.out.println("TabelaCustosIndices salvar iniciado ");
		
		TabelaCustosIndices objectReturn = null;

		ConvertXlsToObj convertXlsToObj = new ConvertXlsToObj();
		TabelaCustosIndices tci = convertXlsToObj.parse(inputStream);

		synchronized (this) {
			System.out.println("TabelaCustosIndices verificando se existe");
			if (find(tci.getNome(), tci.getLocalidade(), tci.getDataPreco()).size() > 0) {
				System.out.println("TabelaCustosIndices já existe");
				throw new br.com.cjt.easybim.controller.exception.ResourceAlreadyExistsException(
						"Resource already exists");
			}

			System.out.println("TabelaCustosIndices salvar insumos");
			tci.getInsumos().forEach(e -> saveInsumo(e));
			System.out.println("TabelaCustosIndices salvar composicoes");
			saveComposicoes(tci.getComposicoes());
			System.out.println("TabelaCustosIndices salva");
			
			objectReturn = save(tci);			
		}
		return objectReturn;
	}

	public void delete(TabelaCustosIndices tabelaCustosIndices) {
		tabelaCustosIndicesRepository.delete(tabelaCustosIndices);
	}

	/**
	 * salva uma composição e todos os itens que a compõe método sincronizado para
	 * garantir que as composições sejam salvas apenas uma única vez, pois elas
	 * possuem relacionamentos e podem fazer parte de muitas outras composições.
	 * 
	 * @param c
	 * @return
	 */
	public synchronized void saveComposicoes(List<Composicao> lc) {
		savedComposicao = new HashSet<>();
		lc.forEach(e -> e = saveComposicaoRecursive(e));
	}

	/**
	 * Salva a composição recursivamente
	 * 
	 * @param c
	 * @return
	 */
	private Composicao saveComposicaoRecursive(Composicao c) {

		for (ItemComposicao ic : c.getItensComposicao()) {
			if (ic.getComposicao() != null && !savedComposicao.contains(ic.getComposicao())) {
				saveComposicaoRecursive(ic.getComposicao());
			}
		}
		savedComposicao.add(c);
		return mongoTemplate.save(c);
	}

	/**
	 * Salva o insumo e seu objetivo representativo(quando o representativo tem id
	 * null) se houver
	 * 
	 * @param i
	 * @return
	 */
	public Insumo saveInsumo(Insumo i) {
		if (i.getInsumoRepresentativo() != null && i.getInsumoRepresentativo().getId() == null) {
			mongoTemplate.save(i.getInsumoRepresentativo());
		}
		return mongoTemplate.save(i);
	}

	public Person findComposicaoByCodigo(TabelaCustosIndices tabelaCustosIndices, String codigo) {
		// return tabelaCustosIndicesRepository.findByCodigo(codigo).orElseThrow( () ->
		// new ResourceNotFoundException(COULD_NOT_FIND + codigo) );
		return null;
	}
}
