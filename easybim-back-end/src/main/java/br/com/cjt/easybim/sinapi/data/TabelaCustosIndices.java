package br.com.cjt.easybim.sinapi.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import br.com.cjt.easybim.data.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode (callSuper = true)
@Document
public class TabelaCustosIndices extends AbstractEntity {

	@Id
	private String id;
	@Field
	private String nome;
	@Field
	private boolean desonerado;
	@Field
	private String abrangencia;
	@Field
	private String referenciaColeta;
	@Field
	private Date dataReferenciaTecnica;
	@Field
	private String localidade;
	@Field
	private String dataPreco;
		
	@ToString.Exclude
	@DBRef (lazy = false)
	private List<Composicao> composicoes = new ArrayList<Composicao>();
	
	@ToString.Exclude
	@DBRef (lazy = false)
	private List<Insumo> insumos = new ArrayList<Insumo>();
}
