package br.com.cjt.easybim.sinapi.data;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
	private String codigoInsumo;
	@Field
	private String descricaoInsumo;
	@Field
	private String unidadeInsumo;
	@Field
	private String origemPrecoInsumo;
	@Field
	private double precoMedianoInsumo;
	
}
