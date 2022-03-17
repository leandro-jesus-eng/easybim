package br.com.cjt.easybim.sinapi.data;

import lombok.Data;

@Data
public class Insumo {

	private String codigoInsumo;
	private String descricaoInsumo;
	private String unidadeInsumo;
	private String origemPrecoInsumo;
	private double precoMedianoInsumo;

}
