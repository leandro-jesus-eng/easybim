package br.com.cjt.easybim.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.cjt.easybim.service.TabelaCustosIndicesService;
import br.com.cjt.easybim.sinapi.data.Composicao;
import br.com.cjt.easybim.sinapi.data.NomeTabelas;
import br.com.cjt.easybim.sinapi.data.TabelaCustosIndices;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TabelaCustosIndicesRESTController {

	@Autowired
	private TabelaCustosIndicesService tabelaCustosIndicesService;

	@Operation (summary = "Upload SINAPI table" )
	@PostMapping("/REST/tabelacustosindices")
	@ResponseStatus(value = HttpStatus.OK)
	public void handleFileUpload(@RequestParam("user-file") MultipartFile file, RedirectAttributes redirectAttributes) {		
		try {
			tabelaCustosIndicesService.save(file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Operation (summary = "Return tables" )
	@GetMapping("/REST/tabelacustosindices")
	public List<TabelaCustosIndices> find(String nameTable, String localidade, String dataPreco) {		
		return tabelaCustosIndicesService.find(nameTable, localidade, dataPreco); 
	}
	
	@Operation (summary = "Return composicao" )
	@GetMapping("/REST/tabelacustosindices/composicoes")
	public List<Composicao> findComposicao(String nameTable, String localidade, String dataPreco) {
		return tabelaCustosIndicesService.findComposicoes(nameTable, localidade, dataPreco); 
	}
	
	@Operation (summary = "Return composicao" )
	@GetMapping("/REST/tabelacustosindices/composicoes/{id}")
	public Composicao findComposicaoById(@PathVariable String id) {
		return tabelaCustosIndicesService.findComposicaoById(id); 
	}
	
	@Operation (summary = "Return dataPreco of table" )
	@GetMapping("/REST/tabelacustosindices/dataPrecos/{nameTable}")
	public List<String> findDataPrecos(@PathVariable  String nameTable) {		
		return tabelaCustosIndicesService.findDataPrecos(nameTable); 
	}
	
	@Operation (summary = "Return name of tables" )
	@GetMapping("/REST/tabelacustosindices/nomeTabelas")	
	public List<NomeTabelas> findNomeTabelas() {
		return tabelaCustosIndicesService.findNomeTabelas();
	}
	
	@Operation (summary = "Return localidades of tables" )
	@GetMapping("/REST/tabelacustosindices/localidades/{nameTable}")
	public List<String> findLocalidades(@PathVariable String nameTable) {
		return tabelaCustosIndicesService.findLocalidades(nameTable);
	}
	
}