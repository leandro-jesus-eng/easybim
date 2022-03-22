package br.com.cjt.easybim.sinapi.data;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document
public class ItemComposicao {
	
	@Field
	private String codigoItem;
	@Field
	private String descricaoItem;
	@Field
	private String unidadeItem;
	@Field
	private String origemPrecoItem;
	@Field
	private double precoUnitarioItem;
	@Field
	private String tipoItem;
	@Field
	private double coeficienteItem;	
	/**
	 * custoTotalItem = coeficienteItem * precoUnitarioItem
	 */
	@Field
	private double custoTotalItem;
	@DBRef (lazy = false)
	private Composicao composicao;
	@DBRef (lazy = false)
	private Insumo insumo;
}
