package br.com.cjt.easybim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cjt.easybim.data.Person;
import br.com.cjt.easybim.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("person")
public class PersonRESTController {

	@Autowired
	private PersonService personService;

	@Operation (summary = "Return all registered people" )
	@GetMapping
	public List<Person> findAll() {
		return personService.findAll();
	}

	@Operation (summary = "Save or update a person" )
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	//@RequestMapping(value = "/REST/person", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public Person save(@RequestBody Person person) {
		return personService.save(person);
	}

	@Operation (summary = "Return a person by Id" )
	@GetMapping("/{id}")
	public Person findById(@PathVariable String id) {
		return personService.findById(id);
	}

	@Operation (summary = "Replace a person by Id" )
	@PutMapping("/{id}")
	public Person replace(@RequestBody Person newPerson, @PathVariable String id) {
		return personService.replace(newPerson, id);
	}
	
	@Operation (summary = "Delete a person by Id" )
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		personService.deleteById(id);
	}
	
	@Operation (summary = "Return all people by first name" )
	@GetMapping("/firstname/{firstName}")
	public List<Person> findByFirstName(@PathVariable String firstName) {
		
		List<Person> p = personService.findByFirstName(firstName); 
		
		return p;
	}
}