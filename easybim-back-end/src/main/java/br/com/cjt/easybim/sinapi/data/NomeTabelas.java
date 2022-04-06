package br.com.cjt.easybim.sinapi.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class NomeTabelas {
	
	public static String SINAPI = "SINAPI";
	public static String SICRO = "SICRO";
	public static String PROPRIA = "PRÓPRIA";

    private String name;
    
    public NomeTabelas(String name) {
    	this.name = name;
    }
    
    public static List<NomeTabelas> values() {
    	List<NomeTabelas> list = new ArrayList<NomeTabelas>();
    	list.add(new NomeTabelas(SINAPI));
    	list.add(new NomeTabelas(SICRO));
    	list.add(new NomeTabelas(PROPRIA));
    	
    	return list;
    }
}
