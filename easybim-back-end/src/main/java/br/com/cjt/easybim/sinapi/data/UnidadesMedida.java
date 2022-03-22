package br.com.cjt.easybim.sinapi.data;

public enum UnidadesMedida {
	
	SC25KG("Saco de 25KG"),
	PAR("Par"),
	MXMES("Metro por Mês"),
	MIL("Mil"),
	M2XMES("Metro Quadrado por Mês"),
	M2("Metro Quadrado"),
	KW_H("KW/H"),
	CJ("Conjunto"),
	CENTO("Cento"),
	_310ML("310 Mililitros"),
	_100M("100 Metros"),
	H("Hora"),
	MES("Mês"),
	M("Metro"),
	JG("Jogo"),
	T("Tonelada"),
	L("Litro"),
	KG("Quilograma"),
	M3("Metro Cúbico"),
	UN("Unidade");

    private String text;

    UnidadesMedida(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
