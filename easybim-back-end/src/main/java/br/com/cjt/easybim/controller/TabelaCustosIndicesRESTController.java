package br.com.cjt.easybim.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("tabelacustosindices")
public class TabelaCustosIndicesRESTController {

	@Autowired
	private TabelaCustosIndicesService tabelaCustosIndicesService;

	@Operation(summary = "Upload SINAPI table")
	@PostMapping
	@ResponseStatus(value = HttpStatus.OK)
	@CacheEvict(value = "listTabelaCustosIndices", allEntries = true)
	public void handleFileUpload(@RequestParam("user-file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			tabelaCustosIndicesService.save(file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Operation(summary = "Return tables")
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	@Cacheable(value = "listTabelaCustosIndices")
	public List<TabelaCustosIndices> find(@RequestParam String nameTable, @RequestParam String localidade, @RequestParam String dataPreco) {
		return tabelaCustosIndicesService.find(nameTable, localidade, dataPreco);
	}

	@Operation(summary = "Return composicao")
	@GetMapping("/composicoes")
	@ResponseStatus(value = HttpStatus.OK)
	@Cacheable(value = "listComposicoes")
	public Page<Composicao> findComposicao(@RequestParam String nameTable, @RequestParam String localidade, @RequestParam String dataPreco,
			@PageableDefault(sort = "id", direction = Direction.ASC) Pageable pagination) {

		// Pageable pagination = null;
		// if(qtd >= 0) pagination = PageRequest.of(page, qtd);
		return tabelaCustosIndicesService.findComposicoes(nameTable, localidade, dataPreco, pagination);
	}

	@Operation(summary = "Return composicao")
	@GetMapping("/composicoes/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Composicao findComposicaoById(@PathVariable String id) {
		return tabelaCustosIndicesService.findComposicaoById(id);
	}

	@Operation(summary = "Return dataPreco of table")
	@GetMapping("/dataPrecos/{nameTable}")
	@ResponseStatus(value = HttpStatus.OK)
	public List<String> findDataPrecos(@PathVariable String nameTable) {
		return tabelaCustosIndicesService.findDataPrecos(nameTable);
	}

	@Operation(summary = "Return name of tables")
	@GetMapping("/nomeTabelas")
	@ResponseStatus(value = HttpStatus.OK)
	public List<NomeTabelas> findNomeTabelas() {
		return tabelaCustosIndicesService.findNomeTabelas();
	}

	@Operation(summary = "Return localidades of tables")
	@GetMapping("/localidades/{nameTable}")
	@ResponseStatus(value = HttpStatus.OK)
	public List<String> findLocalidades(@PathVariable String nameTable) {
		return tabelaCustosIndicesService.findLocalidades(nameTable);
	}
}