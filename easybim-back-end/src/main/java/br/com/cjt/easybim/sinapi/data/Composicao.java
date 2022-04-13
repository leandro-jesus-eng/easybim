package br.com.cjt.easybim.sinapi.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.cjt.easybim.data.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode (callSuper = true)
@Document
public class Composicao extends AbstractEntity  {
	
	@Id
	private String id;
	
	@Field
	private String tabelaCustosIndicesId;
	
	@Field
	private String descricaoClasse;
	@Field
	private String siglaClasse;
	@Field
	private String descricaoTipo1;
	@Field
	private String siglaTipo1;
	@Field
	private String codAgrupador;
	@Field
	private String descricaoAgrupador;
	@Field
	private String codigoComposicao;
	@Field
	private String descricaoComposicao;
	@Field
	private String unidade;
	@Field
	private String origemPreco;
	@Field
	private double custoTotal;
	@Field
	private double custoMaoObra;
	@Field
	private double percentualMaoObra;
	@Field
	private double custoMaterial;
	@Field
	private double percentualMaterial;
	@Field
	private double custoEquipamento;
	@Field
	private double percentualEquipamento;
	@Field
	private double custoServicosTerceiros;
	@Field
	private double percentualServicosTerceiros;
	@Field
	private double custosOutros;
	@Field
	private double percentualOutros;
	@Field
	private double percentualAtribuidoSaoPaulo;
	@Field
	private String vinculo;

	@Field
	@ToString.Exclude
	@JsonManagedReference
	private List<ItemComposicao> itensComposicao = new ArrayList<ItemComposicao>();

}
