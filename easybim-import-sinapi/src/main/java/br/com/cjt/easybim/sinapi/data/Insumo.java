package br.com.cjt.easybim.sinapi.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode (callSuper = true)
public class Insumo extends ItensComposicao {
	
	 private String codigoInsumo;
	 private String descricaoInsumo;
	 private String unidadeInsumo;
	 private OrigemPreco origemPrecoInsumo;
	 private double precoMedianoInsumo;
}
