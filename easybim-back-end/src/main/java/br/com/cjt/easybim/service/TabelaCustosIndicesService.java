package br.com.cjt.easybim.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.client.model.Filters;

import br.com.cjt.easybim.controller.exception.ResourceNotFoundException;
import br.com.cjt.easybim.repository.ComposicaoRepository;
import br.com.cjt.easybim.repository.TabelaCustosIndicesRepository;
import br.com.cjt.easybim.sinapi.ConvertXlsToObj;
import br.com.cjt.easybim.sinapi.data.Composicao;
import br.com.cjt.easybim.sinapi.data.Insumo;
import br.com.cjt.easybim.sinapi.data.ItemComposicao;
import br.com.cjt.easybim.sinapi.data.NomeTabelas;
import br.com.cjt.easybim.sinapi.data.TabelaCustosIndices;

//TODO criar composições próprias baseadas no SINAPI. guardar de onde saiu, histórico
//TODO criar insumos próprios baseadas no SINAPI. guardar de onde saiu, histórico

//TODO Importar tabelas, ler os dados diretamente do arquivo compactado

//TODO Insumo, adicionar MACRO Classe - importante para fazer o histograma
//TODO Insumo, coeficiente desconhecido q está na família de insumos serve para calcular o valor baseado em um item de agregação
//TODO Insumo, verificar onde estão os insumos e composições que estão sem custo ... verificar o arquivo excel catálogo de referenciais

//TODO Orçamento, precisa confrontar o atribuído a São Paulo, adicionar o percentual atribuído a São Paulo

//TODO Service, testar @Transactional para o mongoDb
//TODO Service, evitar o uso de objetos da classe para não haver problemas de alteração de estado de objetos por requisições concorrentes 

//TODO Constroller, testar se a API é idempotent rest
//TODO Controller, customizar o tratamento de erros
//TODO Controller, implementar paginação dos listAll, alterar os repository para extender de paginação

//TODO Repository, testar se as querys são geradas automaticamente quando utilizado o findBy'nome do campo' 

@Service
@Transactional
public class TabelaCustosIndicesService {

	public static String COULD_NOT_FIND = "Could not find = ";

	private Set<Object> savedComposicao = null;

	@Autowired
	private TabelaCustosIndicesRepository tabelaCustosIndicesRepository;

	@Autowired
	private ComposicaoRepository composicaoRepository;

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
	
	public Composicao findComposicaoById(@NotNull String id) {
		return composicaoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(COULD_NOT_FIND + "Composicao id =" + id));
	}

	/**
	 * 
	 * @return
	 */
	public List<NomeTabelas> findNomeTabelas() {
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
		Bson filter = Filters.eq("nome", nameTable);
		mongoTemplate.getCollection("tabelaCustosIndices").distinct("dataPreco", filter, String.class).iterator()
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

		/*
		 * List<TabelaCustosIndices> tcis = new ArrayList<TabelaCustosIndices>();
		 * BasicQuery query = new BasicQuery("nome:"+nameTable);
		 * mongoTemplate.findDistinct(query, "localidade", "tabelaCustosIndices",
		 * TabelaCustosIndices.class) .iterator() .forEachRemaining(tcis::add);
		 */
		// tcis.iterator().forEachRemaining(x ->
		// localidadesList.add(x.getLocalidade()));
		// localidadesList = tcis.stream().map(x ->
		// x.getNome()).collect(Collectors.toList());

		// código para o mongo =
		// db.getCollection('tabelaCustosIndices').distinct('localidade',
		// {nome:"SINAPI"})
		Bson filter = Filters.eq("nome", nameTable);
		mongoTemplate.getCollection("tabelaCustosIndices").distinct("localidade", filter, String.class).iterator()
				.forEachRemaining(localidadesList::add);

		if (localidadesList.size() == 0)
			throw new ResourceNotFoundException(COULD_NOT_FIND + nameTable);

		return localidadesList;
	}

	public List<Composicao> findComposicoes(String nameTable, String localidade, String dataPreco) {
		List<TabelaCustosIndices> ltci = find(nameTable, localidade, dataPreco);

		if (ltci == null || ltci.size() == 0)
			throw new ResourceNotFoundException(COULD_NOT_FIND + "nameTable=" + nameTable + " localidade=" + localidade
					+ " dataPreco=" + dataPreco);
		
		return composicaoRepository
				.find(ltci.get(0).getId())
				.orElseThrow(() -> new ResourceNotFoundException(COULD_NOT_FIND + "Composicao - tabelaCustosIndicesId=" + ltci.get(0).getId() )); 		

		/*BasicQuery query = new BasicQuery("tabelaCustosIndicesId : '"+ltci.get(0).getId()+"'");		
		return StreamSupport
				.stream(mongoTemplate.find(query, Composicao.class, "composicao").spliterator(), false)
				.collect(Collectors.toList());*/		
		
		/*Bson filter = Filters.eq("tabelaCustosIndicesId", ltci.get(0).getId());
		Bson projection = Projections.fields(Projections.include("composicao") );				
		return StreamSupport
				.stream(mongoTemplate.getCollection("composicao").find(filter,  Composicao.class).projection(projection).spliterator(), false)
				.collect(Collectors.toList());*/
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

		TabelaCustosIndices tciSaved = null;

		ConvertXlsToObj convertXlsToObj = new ConvertXlsToObj();
		TabelaCustosIndices tci = convertXlsToObj.parse(inputStream);

		synchronized (this) {
			System.out.println("TabelaCustosIndices verificando se existe");
			if (find(tci.getNome(), tci.getLocalidade(), tci.getDataPreco()).size() > 0) {
				System.out.println("TabelaCustosIndices já existe");
				throw new br.com.cjt.easybim.controller.exception.ResourceAlreadyExistsException(
						"Resource already exists");
			}

			List<Insumo> li = tci.getInsumos();
			List<Composicao> lc = tci.getComposicoes();
			tci.setInsumos(null);
			tci.setComposicoes(null);

			save(tci);
			tci.setInsumos(li);
			tci.setComposicoes(lc);

			System.out.println("TabelaCustosIndices salvar insumos");
			tci.getInsumos().forEach(e -> e.setTabelaCustosIndicesId(tci.getId()));
			tci.getInsumos().forEach(e -> saveInsumo(e));
			System.out.println("TabelaCustosIndices salvar composicoes");
			tci.getComposicoes().forEach(e -> e.setTabelaCustosIndicesId(tci.getId()));
			saveComposicoes(tci.getComposicoes());
			System.out.println("TabelaCustosIndices salva");

			tciSaved = save(tci);
		}
		return tciSaved;
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
				ic.setId(ic.getComposicao().getId());
			} else { 
				if(ic.getTipoItem().equals(ConvertXlsToObj.TIPO_INSUMO))
					ic.setId(ic.getInsumo().getId());
			}
		}
		savedComposicao.add(c);
		return composicaoRepository.save(c);
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
}
