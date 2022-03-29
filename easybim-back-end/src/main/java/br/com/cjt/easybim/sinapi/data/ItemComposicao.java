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
	
	// lazy false trava o mongodb, Teoricamente, não era pra entrar em loop infinito, 
	// mas o consulta não retorna depois de vários minutos.
	@DBRef (lazy = true)
	private Composicao composicao;
	
	// lazy false trava o mongodb, Teoricamente, não era pra entrar em loop infinito, 
	// mas o consulta não retorna depois de vários minutos.
	@DBRef (lazy = true)
	private Insumo insumo;
}
