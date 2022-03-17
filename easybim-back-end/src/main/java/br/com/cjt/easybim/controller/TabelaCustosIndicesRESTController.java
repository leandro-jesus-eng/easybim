package br.com.cjt.easybim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class TabelaCustosIndicesRESTController {

	@Autowired
	//private StorageService storageService;

	@Operation (summary = "Upload SINAPI table" )
	@PostMapping("/REST/tabelacustosindices")
	@ResponseStatus(value = HttpStatus.OK)
	public void handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		
		//storageService.store(file);		
	}
}