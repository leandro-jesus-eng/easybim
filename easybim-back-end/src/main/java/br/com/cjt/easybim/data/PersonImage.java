package br.com.cjt.easybim.data;

import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode (callSuper = true)

@Document
public class PersonImage extends AbstractEntity {

	@Id
	private String id;
	
	@Lob
	@Field // @Column (columnDefinition = "BLOB")	
	private byte[] image;
	
	@Field
	private String contentType;	
	
	@Field
	private String nameImage;	
	
	//@ManyToOne (fetch = FetchType.EAGER)
	/*@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonBackReference
	private Person person;*/
}
