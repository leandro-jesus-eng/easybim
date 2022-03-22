package br.com.cjt.easybim.sinapi.data;

public enum OrigemPreco {
	
	COEFICIENTE_REPRESENTATIVIDADE("Coeficiente de Representatividade"),
	COLETADO("Coletado"),
	ATRIBUIDO_SAO_PAULO("Atribu�do S�o Paulo");

    private String text;

    OrigemPreco(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
