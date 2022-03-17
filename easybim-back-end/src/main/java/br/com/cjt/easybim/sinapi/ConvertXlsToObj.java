package br.com.cjt.easybim.sinapi;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.aspose.cells.Cells;
import com.aspose.cells.Range;
import com.aspose.cells.Workbook;

import br.com.cjt.easybim.sinapi.data.Composicao;
import br.com.cjt.easybim.sinapi.data.Insumo;
import br.com.cjt.easybim.sinapi.data.ItemComposicao;
import br.com.cjt.easybim.sinapi.data.TabelaCustosIndices;

public class ConvertXlsToObj {

	private TabelaCustosIndices tabelaCustosIndices = null;
	private Workbook workbook = null;
	private Range range = null;

	static int desoneradoRowIndex = 0;
	static int abrangenciaRowIndex = 1;
	static int referenciaColetaRowIndex = 2;
	static int dataReferenciaTecnicaRowIndex = 3;
	static int localidadeRowIndex = 4;
	static int dataPrecoRowIndex = 5;
	static int rowHeader = 6;

	static int descricaoClasseColumnIndex = 0;
	static int siglaClasseColumnIndex = 1;
	static int descricaoTipo1ColumnIndex = 2;
	static int siglaTipo1ColumnIndex = 3;
	static int codigoComposicaoColumnIndex = 6;
	static int descricaoComposicaoColumnIndex = 7;
	static int unidadeColumnIndex = 8;
	static int origemPrecoColumnIndex = 9;
	static int custoTotalColumnIndex = 10;
	static int custoMaoObraColumnIndex = 19;
	static int percentualMaoObraColumnIndex = 20;
	static int custoMaterialColumnIndex = 21;
	static int percentualMaterialColumnIndex = 22;
	static int custoEquipamentoColumnIndex = 23;
	static int percentualEquipamentoColumnIndex = 24;
	static int custoServicosTerceirosColumnIndex = 25;
	static int percentualServicosTerceirosColumnIndex = 26;
	static int custosOutrosColumnIndex = 27;
	static int percentualOutrosColumnIndex = 28;
	static int percentualAtribuidoSaoPauloColumnIndex = 29;
	static int vinculoColumnIndex = 30;

	static int tipoItemColumnIndex = 11;
	static int codigoItemColumnIndex = 12;
	static int descricaoItemColumnIndex = 13;
	static int unidadeItemColumnIndex = 14;
	static int origemPrecoItemColumnIndex = 15;
	static int coeficienteColumnIndex = 16;
	static int precoUnitarioColumnIndex = 17;
	static int custoTotalItemColumnIndex = 18;

	static String TIPO_COMPOSICAO = "COMPOSICAO";
	static String TIPO_INSUMO = "INSUMO";

	static int codigoInsumoColumnIndex = 0;
	static int descricaoInsumoColumnIndex = 1;
	static int unidadeInsumoColumnIndex = 2;
	static int origemPrecoInsumoColumnIndex = 3;
	static int precoMedianoInsumoColumnIndex = 4;

	private Map<String, Insumo> insumoMap = null;

	private Map<String, Composicao> composicaoMap = null;

	private String getStringValue(int row, int column) {
		return range.get(row, column).getStringValue().trim();
	}

	private double getDoubleValue(int row, int column) {
		try {
			return Double.parseDouble(getStringValue(row, column).replaceAll("%", "").replaceAll("\\.", "").replace(',', '.'));
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Error value = "+getStringValue(row, column)+" row="+row+"   replaced="+getStringValue(row, column).replaceAll("%", "").replaceAll("\\.", "").replace(',', '.'));
			e.printStackTrace();
		}
		return 0;
	}

	private Composicao getComposicaoIfExistsOrAddNew(String codigo) {
		Composicao composicao = null;
		if (composicaoMap.containsKey(codigo))
			composicao = composicaoMap.get(codigo);
		else {
			composicao = new Composicao();
			composicao.setCodigoComposicao(codigo);
			tabelaCustosIndices.getComposicoes().add(composicao);
			composicaoMap.put(codigo, composicao);
		}
		return composicao;
	}

	private void parseInsumos() {
		// access CellsCollection of the worksheet containing data to be converted
		Cells cells = workbook.getWorksheets().get(1).getCells();

		// create a range of cells containing data to be exported
		range = cells.createRange(0, 0, cells.getLastCell().getRow() + 1, cells.getLastCell().getColumn() + 1);

		insumoMap = new HashMap<String, Insumo>(range.getRowCount());

		for (int row = 1; row < range.getRowCount(); row++) {
			Insumo insumo = new Insumo();
			insumo.setCodigoInsumo(getStringValue(row, codigoInsumoColumnIndex));
			insumo.setDescricaoInsumo(getStringValue(row, descricaoInsumoColumnIndex));
			insumo.setUnidadeInsumo(getStringValue(row, unidadeInsumoColumnIndex));
			insumo.setOrigemPrecoInsumo(getStringValue(row, origemPrecoInsumoColumnIndex));
			insumo.setPrecoMedianoInsumo(getDoubleValue(row, precoMedianoInsumoColumnIndex));

			tabelaCustosIndices.getInsumos().add(insumo);
			insumoMap.put(insumo.getCodigoInsumo(), insumo);
		}
	}

	private void parseTabelaCustosIndices() {
		// inicia a minha arvore com a tabela de custos
		tabelaCustosIndices.setDesonerado(Boolean.parseBoolean(getStringValue(desoneradoRowIndex, 1)));
		tabelaCustosIndices.setAbrangencia(getStringValue(abrangenciaRowIndex, 1));
		tabelaCustosIndices.setReferenciaColeta(getStringValue(referenciaColetaRowIndex, 1));
		try {
			tabelaCustosIndices.setDataReferenciaTecnica(
					new SimpleDateFormat("dd/MM/yyyy").parse(getStringValue(dataReferenciaTecnicaRowIndex, 1)));
			tabelaCustosIndices.setLocalidade(getStringValue(localidadeRowIndex, 1));
			Date datap = new SimpleDateFormat("dd/MM/yyyy").parse(getStringValue(dataPrecoRowIndex, 1));
			tabelaCustosIndices.setDataPreco(new SimpleDateFormat("MMMM/yyyy", new Locale("pt", "BR")).format(datap));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		tabelaCustosIndices.setId(null);
	}

	private void parseComposicoes() {

		// access CellsCollection of the worksheet containing data to be converted
		Cells cells = workbook.getWorksheets().get(0).getCells();

		// create a range of cells containing data to be exported
		range = cells.createRange(0, 0, cells.getLastCell().getRow() + 1, cells.getLastCell().getColumn() + 1);

		parseTabelaCustosIndices();

		Composicao composicao = null;
		composicaoMap = new HashMap<String, Composicao>(range.getRowCount());

		// range.getRowCount() = 30
		for (int row = (rowHeader + 1); row < range.getRowCount(); row++) {

			// se a coluna codigoItem(12) for vazia ent�o � uma composicao PAI
			if (range.get(row, codigoItemColumnIndex).getStringValue().isEmpty()) {

				composicao = getComposicaoIfExistsOrAddNew(getStringValue(row, codigoComposicaoColumnIndex));

				composicao.setCodigoComposicao(getStringValue(row, codigoComposicaoColumnIndex));
				composicao.setDescricaoClasse(getStringValue(descricaoClasseColumnIndex, 0));
				composicao.setSiglaClasse(getStringValue(row, siglaClasseColumnIndex));
				composicao.setDescricaoTipo1(getStringValue(row, descricaoTipo1ColumnIndex));
				composicao.setSiglaTipo1(getStringValue(row, siglaTipo1ColumnIndex));
				composicao.setDescricaoComposicao(getStringValue(row, descricaoComposicaoColumnIndex));
				composicao.setUnidade(getStringValue(row, unidadeColumnIndex));
				composicao.setOrigemPreco(getStringValue(row, origemPrecoColumnIndex));
				composicao.setCustoTotal(getDoubleValue(row, custoTotalColumnIndex));
				composicao.setCustoMaoObra(getDoubleValue(row, custoMaoObraColumnIndex));
				composicao.setPercentualMaoObra(getDoubleValue(row, percentualMaoObraColumnIndex));
				composicao.setCustoMaterial(getDoubleValue(row, custoMaterialColumnIndex));
				composicao.setPercentualMaterial(getDoubleValue(row, percentualMaterialColumnIndex));
				composicao.setCustoEquipamento(getDoubleValue(row, custoEquipamentoColumnIndex));
				composicao.setPercentualEquipamento(getDoubleValue(row, percentualEquipamentoColumnIndex));
				composicao.setCustoServicosTerceiros(getDoubleValue(row, custoServicosTerceirosColumnIndex));
				composicao.setPercentualServicosTerceiros(getDoubleValue(row, percentualServicosTerceirosColumnIndex));
				composicao.setCustosOutros(getDoubleValue(row, custosOutrosColumnIndex));
				composicao.setPercentualOutros(getDoubleValue(row, percentualOutrosColumnIndex));
				composicao.setPercentualAtribuidoSaoPaulo(getDoubleValue(row, percentualAtribuidoSaoPauloColumnIndex));
				composicao.setVinculo(getStringValue(row, vinculoColumnIndex));

			} else { // ent�o � um item da composicao

				ItemComposicao itemComposicao = new ItemComposicao();
				itemComposicao.setCodigoItem(getStringValue(row, codigoItemColumnIndex));
				itemComposicao.setTipoItem(getStringValue(row, tipoItemColumnIndex));
				itemComposicao.setDescricaoItem(getStringValue(row, descricaoItemColumnIndex));
				itemComposicao.setUnidadeItem(getStringValue(row, unidadeItemColumnIndex));
				itemComposicao.setOrigemPrecoItem(getStringValue(row, origemPrecoItemColumnIndex));
				itemComposicao.setCoeficienteItem(getDoubleValue(row, coeficienteColumnIndex));
				itemComposicao.setPrecoUnitarioItem(getDoubleValue(row, precoUnitarioColumnIndex));
				itemComposicao.setCustoTotalItem(getDoubleValue(row, custoTotalItemColumnIndex));

				// verificar se � uma composicao ou insumo
				if (itemComposicao.getTipoItem().equals(TIPO_COMPOSICAO)) {
					itemComposicao.setComposicao(getComposicaoIfExistsOrAddNew(itemComposicao.getCodigoItem()));
				} else {
					itemComposicao.setInsumo(insumoMap.get(itemComposicao.getCodigoItem()));
				}

				composicao.getItensComposicao().add(itemComposicao);
			}
		}
	}
	
	private void parse() {
		
		tabelaCustosIndices = new TabelaCustosIndices();

		parseInsumos();

		parseComposicoes();
		
		System.out.println("Numero de composicoes no MAP "+ composicaoMap.size() );
		System.out.println("Numero de composicoes no tabela "+ tabelaCustosIndices.getComposicoes().size() );
		System.out.println("Numero de insumos no tabela "+ tabelaCustosIndices.getInsumos().size() );
		System.out.println("Importacao finalizada com sucesso");
	}

	public TabelaCustosIndices parse(InputStream inputStream) {
		try {
			// load XLSX file with an instance of Workbook
			workbook = new Workbook(inputStream);
			parse();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tabelaCustosIndices;
	}
	
	public TabelaCustosIndices parse(String file) {

		tabelaCustosIndices = new TabelaCustosIndices();

		try {
			// load XLSX file with an instance of Workbook
			workbook = new Workbook(file);

			parseInsumos();

			parseComposicoes();
			
			System.out.println("Numero de composicoes no MAP "+ composicaoMap.size() );
			System.out.println("Numero de composicoes no tabela "+ tabelaCustosIndices.getComposicoes().size() );
			System.out.println("Numero de insumos no tabela "+ tabelaCustosIndices.getInsumos().size() );
			System.out.println("Importacao finalizada com sucesso");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tabelaCustosIndices;
	}

	/**/
	public static void main(String[] args) {
		new ConvertXlsToObj().parse("SINAPI_Desonerado.xls");
	}
}
