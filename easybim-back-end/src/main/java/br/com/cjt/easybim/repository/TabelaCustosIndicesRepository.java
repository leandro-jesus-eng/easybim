package br.com.cjt.easybim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.com.cjt.easybim.sinapi.data.TabelaCustosIndices;

public interface TabelaCustosIndicesRepository extends  MongoRepository<TabelaCustosIndices, String>, EntityRepository<TabelaCustosIndices> {
	
	@Query("{ nome : '?0', localidade : '?1', dataPreco : '?2'  }")	 
	public Optional<List<TabelaCustosIndices>> find(String nameTable, String localidade, String dataPreco);
}
