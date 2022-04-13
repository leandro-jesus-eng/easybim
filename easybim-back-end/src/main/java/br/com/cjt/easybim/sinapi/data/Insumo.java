package br.com.cjt.easybim.sinapi.data;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.cjt.easybim.data.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode (callSuper = true)
@Document
public class Insumo extends AbstractEntity {

	@Id
	private String id;
	
	@Field
	private String tabelaCustosIndicesId;
	
	@Field
	private String codigoInsumo;
	@Field
	private String descricaoInsumo;
	@Field
	private String unidadeInsumo;
	@Field
	private String origemPrecoInsumo;
	@Field
	private double precoMedianoInsumo;

	// lazy false trava o mongodb, pois busca o insumo representativo ao mesmo tempo causando vários loops. 
	// Teoricamente, não era pra entrar em loop infinito, mas o consulta não retorna depois de vários minutos.
	@DBRef (lazy = true)
	@JsonBackReference
	private Insumo insumoRepresentativo;
	@Field
	private double coeficienteInsumoRepresentativo;
	@Field
	private String categoria;
	@Field
	private String macroClasse;
	@Field
	private String vinculo;	
}
