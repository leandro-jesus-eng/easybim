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

import br.com.cjt.easybim.controller.ResourceNotFoundException;
import br.com.cjt.easybim.data.Person;
import br.com.cjt.easybim.repository.TabelaCustosIndicesRepository;
import br.com.cjt.easybim.sinapi.ConvertXlsToObj;
import br.com.cjt.easybim.sinapi.data.Composicao;
import br.com.cjt.easybim.sinapi.data.ItemComposicao;
import br.com.cjt.easybim.sinapi.data.NomeTabelas;
import br.com.cjt.easybim.sinapi.data.TabelaCustosIndices;

//TODO criar composições próprias baseadas no SINAPI. guardar de onde saiu, histórico
//TODO criar insumos próprios baseadas no SINAPI. guardar de onde saiu, histórico
//TODO Insumo, adicionar MACRO Classe - importante para fazer o histograma
//TODO Insumo, coeficiente desconhecido q está na família de insumos serve para calcular o valor baseado em um item de agregação
//TODO Orçamento precisa confrontar o atribuído a São Paulo, adicionar o percentual atribuído a São Paulo
//TODO Verificar onde estão os insumos e composições que estão sem custo ... verificar o arquivo excel catálogo de referenciais
//TODO testar @Transactional
//TODO verificar se um @Service é criado para cada conexão

@Service
@Transactional
public class TabelaCustosIndicesService {

	public static String COULD_NOT_FIND = "Could not find = ";

	private Set<Object> savedObjects = null;

	@Autowired
	private TabelaCustosIndicesRepository tabelaCustosIndicesRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ConvertXlsToObj convertXlsToObj;

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
			new br.com.cjt.easybim.controller.IllegalArgumentException(
					"Parameters: (nameTable, dataPreco, localidade) cannot be null");

		return tabelaCustosIndicesRepository.find(nameTable, localidade, dataPreco)
				.orElseThrow(() -> new ResourceNotFoundException(COULD_NOT_FIND + "nameTable=" + nameTable
						+ " localidade=" + localidade + " dataPreco=" + dataPreco));
	}

	/**
	 * 
	 * @return
	 */
	public List<String> findNomeTabelas() {
		return Stream.of(NomeTabelas.values()).map(NomeTabelas::name).collect(Collectors.toList());
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
			new ResourceNotFoundException(COULD_NOT_FIND + nameTable);

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
			new ResourceNotFoundException(COULD_NOT_FIND + nameTable);

		return localidadesList;
	}

	/**
	 * 
	 * @param tabelaCustosIndices
	 * @return
	 */
	public TabelaCustosIndices save(TabelaCustosIndices tabelaCustosIndices) {
		return tabelaCustosIndicesRepository.save(tabelaCustosIndices);
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	@Transactional
	public TabelaCustosIndices save(InputStream inputStream) {
		savedObjects = new HashSet<>();

		// TODO, verificar se a tabela já foi importada
		TabelaCustosIndices tci = convertXlsToObj.parse(inputStream);
		tci.getInsumos().forEach(e -> mongoTemplate.save(e));
		tci.getComposicoes().forEach(e -> e = saveComposicao(e));
		return save(tci);
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public Composicao saveComposicao(Composicao c) {
		for (ItemComposicao ic : c.getItensComposicao()) {
			if (ic.getComposicao() != null && !savedObjects.contains(ic.getComposicao())) {
				saveComposicao(ic.getComposicao());
			}
		}
		savedObjects.add(c);
		return mongoTemplate.save(c);
	}

	public void delete(TabelaCustosIndices tabelaCustosIndices) {
		// tabelaCustosIndicesRepository.delete(tabelaCustosIndices);
	}

	public Person findComposicaoByCodigo(String codigo) {
		// return tabelaCustosIndicesRepository.findByCodigo(codigo).orElseThrow( () ->
		// new ResourceNotFoundException(COULD_NOT_FIND + codigo) );
		return null;
	}

	public TabelaCustosIndices replace(TabelaCustosIndices newPerson, String id) {
		if (id == null)
			new br.com.cjt.easybim.controller.IllegalArgumentException("ID parameter cannot be null");

		/*
		 * return personRepository.findById(id).map(person -> {
		 * person.setFirstName(newPerson.getFirstName());
		 * person.setLastName(newPerson.getLastName());
		 * person.setBirthday(newPerson.getBirthday());
		 * person.setGender(newPerson.getGender());
		 * person.setPersonAddresses(newPerson.getPersonAddresses());
		 * person.setPersonImages(newPerson.getPersonImages()); return
		 * personRepository.save(person); }).orElseGet(() -> { new
		 * ResourceNotFoundException("Could not find person id = "+id); return null; });
		 */
		return null;
	}

}
