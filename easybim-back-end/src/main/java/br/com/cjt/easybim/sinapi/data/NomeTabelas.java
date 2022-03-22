package br.com.cjt.easybim.sinapi.data;

public enum NomeTabelas {
	
	SINAPI("SINAPI"),
	SICRO("SICRO"),
	PROPRIA("PRÓPRIA");

    private String text;

    NomeTabelas(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
