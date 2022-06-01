package br.com.cjt.easybim.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.cjt.easybim.sinapi.data.Composicao;

public interface ComposicaoRepository extends MongoRepository<Composicao, String>, EntityRepository<Composicao> {
	
	//@Query("{ tabelaCustosIndicesId : '?0' }")	 
	//public Optional<Page<Composicao>> find(String tabelaCustosIndicesId, Pageable pagination);
	
	public Optional<Page<Composicao>> findByTabelaCustosIndicesId(String tabelaCustosIndicesId, Pageable pagination);
	
	
}
