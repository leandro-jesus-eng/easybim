package br.com.cjt.easybim.sinapi.data;

public enum OrigemPreco {
	
	CR("Coeficiente de Representatividade"),
	C("Coletado"),
	AS("Atribuído São Paulo");

    private String text;

    OrigemPreco(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
