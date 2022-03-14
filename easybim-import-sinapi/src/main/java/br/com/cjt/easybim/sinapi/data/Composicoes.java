package br.com.cjt.easybim.sinapi.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
public class Composicoes {
	
	private String descricaoClasse;
	private String siglaClasse;
	private String descricaoTipo1;
	private String siglaTipo1;
	private String codAgrupador;
	private String descricaoAgrupador;
	private String codigoComposicao;
	private String descricaoComposicao;
	private String unidade;
	private String origemPreco;
	private double custoTotal;	
	private double custoMaoObra;
	private double percentualMaoObra;
	private double custoMaterial;
	private double percentualMaterial;
	private double custoEquipamento;
	private double percentualEquipamento;
	private double custoServicosTerceiros;
	private double percentualServicosTerceiros;
	private double custosOutros;
	private double percentualOutros;
	private double percentualAtribuidoSaoPaulo;
	private String vinculo;

	@ToString.Exclude
	private List<Composicoes> itemComposicao = new ArrayList<Composicoes>();
	
	@ToString.Exclude
	private List<Insumo> itemInsumo = new ArrayList<Insumo>();
}
