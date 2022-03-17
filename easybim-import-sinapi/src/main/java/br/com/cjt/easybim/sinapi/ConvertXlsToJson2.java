package br.com.cjt.easybim.sinapi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.aspose.cells.Cells;
import com.aspose.cells.Range;
import com.aspose.cells.Workbook;

public class ConvertXlsToJson2 {

	public static void main(String[] args) {
		
		int rowHeader = 5;

		int descricaoClasse = 0;
		int siglaClasse = 1;
		int descricaoTipo1 = 2;
		int siglaTipo1 = 3;
		int codigoComposicao = 6;
		int descricaoComposicao = 7;
		int unidade = 8;
		int origemPreco = 9;
		int custoTotal = 10;
		int custoMaoObra = 19;
		int percentualMaoObra = 20;
		int custoMaterial = 21;
		int percentualMaterial = 22;
		int custoEquipamento = 23;
		int percentualEquipamento = 24;
		int custoServicosTerceiros = 25;
		int percentualServicosTerceiros = 26;
		int custosOutros = 27;
		int percentualOutros = 28;
		int percentualAtribuidoSaoPaulo = 29;
		int vinculo = 30;

		int tipoItem = 11;
		int codigoItem = 12;
		int descricaoItem = 13;
		int unidadeItem = 14;
		int origemPrecoItem = 15;
		int coeficiente = 16;
		int precoUnitario = 17;
		int custoTotalItem = 18;

		int indexOfComposicoesColumns[] = { descricaoClasse, siglaClasse, descricaoTipo1, siglaTipo1, codigoComposicao,
				descricaoComposicao, unidade, origemPreco, custoTotal, custoMaoObra, percentualMaoObra, custoMaterial,
				percentualMaterial, custoEquipamento, percentualEquipamento, custoServicosTerceiros,
				percentualServicosTerceiros, custosOutros, percentualOutros, percentualAtribuidoSaoPaulo, vinculo };

		int indexOfComposicaoItensColumns[] = { tipoItem, codigoItem, descricaoItem, unidadeItem, origemPrecoItem,
				coeficiente, precoUnitario, custoTotalItem };

		try {
			// load XLSX file with an instance of Workbook
			Workbook workbook = new Workbook("SINAPI_Desonerado.xls");

			// access CellsCollection of the worksheet containing data to be converted
			Cells cells = workbook.getWorksheets().get(0).getCells();

			// create & set ExportRangeToJsonOptions for advanced options
			// ExportRangeToJsonOptions exportOptions = new ExportRangeToJsonOptions();

			// create a range of cells containing data to be exported
			Range range = cells.createRange(0, 0, cells.getLastCell().getRow() + 1,
					cells.getLastCell().getColumn() + 1);			

			String[] namesColumns = new String[range.getColumnCount()];
			for (int c = 0; c < range.getColumnCount(); c++) {
				namesColumns[c] = range.get(rowHeader, c).getStringValue();
			}

			JSONObject jsonObj = new JSONObject();
			JSONObject composicao = null;
			String codigoComposicaoString = "";
			String codigoComposicaoItemString = "";
			
			for (int row = (rowHeader + 1); row < range.getRowCount(); row++) {
			//for (int row = (rowHeader + 1); row < 30; row++) {				

				// se a coluna codigoItem(12) for vazia então é uma composicao
				if (range.get(row, codigoItem).getStringValue().isEmpty()) {
					
					composicao = new JSONObject();
					jsonObj.accumulate("data", composicao);

					codigoComposicaoString = range.get(row, codigoComposicao).getStringValue().trim();

					for (int i : indexOfComposicoesColumns) {
						String value = range.get(row, i).getStringValue().trim();
						boolean isNumeric =  value.matches("[+-]?\\d*(\\,\\d+)?");
						if(isNumeric)
							composicao.accumulate(namesColumns[i], Double.parseDouble(value.replace(',', '.')));
						else
							composicao.accumulate(namesColumns[i], value);
					}

				} else { // então é um item da composicao

					JSONObject item = new JSONObject();
					if (composicao != null)
						composicao.accumulate("itensComposicao", item);
					else
						System.out.println("Erro na importacao: composicao NULL ");

					codigoComposicaoItemString = range.get(row, codigoComposicao).getStringValue().trim();
					if (!codigoComposicaoString.equals(codigoComposicaoItemString))
						System.out.println("Erro importação: codigoComposicao (" + codigoComposicaoString
								+ ") e codigoItemComposicao ("+codigoComposicaoItemString+")  DIFERENTES!");

					for (int i : indexOfComposicaoItensColumns) {
						String value = range.get(row, i).getStringValue().trim();
						boolean isNumeric =  value.matches("[+-]?\\d*(\\,\\d+)?");
						if(isNumeric)
							item.accumulate(namesColumns[i], Double.parseDouble(value.replace(',', '.')));
						else
							item.accumulate(namesColumns[i], value);						
					}
				}
			}

			String jsonData = jsonObj.toString();

			// write data to disc in JSON format
			BufferedWriter writer = new BufferedWriter(new FileWriter("composicoes.json", StandardCharsets.UTF_8));
			writer.write(jsonData);
			writer.close();
			
			System.out.println("fim da importação ... ");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
}
