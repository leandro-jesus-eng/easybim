package br.com.cjt.easybim.sinapi.data;

import lombok.Data;

@Data
public class ItemComposicao {	
	private String tipoItem;
	private String codigoItem;
	private String descricaoItem;
	private UnidadesMedida unidadeItem;
	private String origemPrecoItem;
	private double coeficiente;
	private double precoUnitario;
	private double custoTotalItem;	
}
