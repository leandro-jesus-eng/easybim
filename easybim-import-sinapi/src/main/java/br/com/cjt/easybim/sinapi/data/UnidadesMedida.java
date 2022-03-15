package br.com.cjt.easybim.sinapi.data;

public enum UnidadesMedida {
	
	SC25KG("Sado de 25KG"),
	PAR("Par"),
	MXMES("Metro por M�s"),
	MIL("Mil"),
	M2XMES("Metro Quadrado por M�s"),
	M2("Metro Quadrado"),
	KW_H("KW/H"),
	CJ("Conjunto"),
	CENTO("Cento"),
	_310ML("310 Mililitros"),
	_100M("100 Metros"),
	H("Hora"),
	MES("M�s"),
	M("Metro"),
	JG("Jogo"),
	T("Tonelada"),
	L("Litro"),
	KG("Quilograma"),
	M3("Metro C�bico"),
	UN("Unidade");

    private String text;

    UnidadesMedida(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
