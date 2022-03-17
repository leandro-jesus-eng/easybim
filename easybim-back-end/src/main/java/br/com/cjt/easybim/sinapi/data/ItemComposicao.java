package br.com.cjt.easybim.sinapi.data;

import lombok.Data;

@Data
public class ItemComposicao {
	
	private String codigoItem;
	private String descricaoItem;
	private String unidadeItem;
	private String origemPrecoItem;
	private double precoUnitarioItem;
	
	private String tipoItem;
	private double coeficienteItem;
	// = coeficienteItem * precoUnitarioItem
	private double custoTotalItem;

	private Composicao composicao;
	private Insumo insumo;
}
