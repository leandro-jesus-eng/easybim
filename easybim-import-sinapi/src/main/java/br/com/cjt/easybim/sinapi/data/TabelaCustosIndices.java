package br.com.cjt.easybim.sinapi.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
//@EqualsAndHashCode (callSuper = true)
public class TabelaCustosIndices {

	private String id;
	
	private boolean desonerado;
	private String abrangencia;
	private String referenciaColeta;
	private Date dataReferenciaTecnica;
	private String localidade;
	private Date dataPreco;
	
	@ToString.Exclude
	private List<Composicao> composicoes = new ArrayList<Composicao>();
}
