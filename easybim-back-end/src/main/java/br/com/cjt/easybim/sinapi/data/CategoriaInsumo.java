package br.com.cjt.easybim.sinapi.data;

public enum CategoriaInsumo {
	
	REPRESENTATIVO("REPRESENTATIVO"),
	REPRESENTADO("REPRESENTADO");

    private String text;

    CategoriaInsumo(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
