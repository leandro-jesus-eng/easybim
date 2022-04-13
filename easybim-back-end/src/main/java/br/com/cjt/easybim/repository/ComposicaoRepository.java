package br.com.cjt.easybim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.com.cjt.easybim.sinapi.data.Composicao;

public interface ComposicaoRepository extends MongoRepository<Composicao, String>, EntityRepository<Composicao> {
	
	@Query("{ tabelaCustosIndicesId : '?0' }")	 
	public Optional<List<Composicao>> find(String tabelaCustosIndicesId);
}
