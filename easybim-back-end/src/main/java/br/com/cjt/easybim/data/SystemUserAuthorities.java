package br.com.cjt.easybim.data;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document
@Data
@EqualsAndHashCode (callSuper = true)
public class SystemUserAuthorities extends AbstractEntity implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Field
	private String name;
	
	@Override
	public String getAuthority() {
		return  name;
	}

}
