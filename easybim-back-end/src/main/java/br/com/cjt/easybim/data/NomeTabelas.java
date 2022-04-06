package br.com.cjt.easybim.data;

import lombok.Data;

@Data
public class NomeTabelas {
	String name;
	
	public NomeTabelas(String name) {
		this.name = name;
	}
}
