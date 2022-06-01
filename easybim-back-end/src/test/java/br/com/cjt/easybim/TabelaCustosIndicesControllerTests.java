package br.com.cjt.easybim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cjt.easybim.controller.exception.ResourceNotFoundException;
import br.com.cjt.easybim.data.Country;
import br.com.cjt.easybim.data.Gender;
import br.com.cjt.easybim.data.Person;
import br.com.cjt.easybim.data.PersonAddress;
import br.com.cjt.easybim.service.PersonService;

@SpringBootTest
@AutoConfigureMockMvc
class TabelaCustosIndicesControllerTests {

	@Autowired
	MongoTemplate mongoTemplate;	 
		
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	PersonService personService;
	
	@Test
	void whenFindAll() throws Exception {
		createData();
		
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person")
			      .accept(MediaType.APPLICATION_JSON))
			      .andDo(MockMvcResultHandlers.print())
			      .andExpect(MockMvcResultMatchers.status().isOk())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Leandro"))
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").isNotEmpty())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[*].personAddresses[*]").isNotEmpty());
		
		destroyData();
	}
	
	@Test
	void whenFindById() throws Exception {
		createData();
		
		Person person;
		Calendar c = Calendar.getInstance();		
		Country country = new Country(null, "Brasil");
		mongoTemplate.save(country);
		
		person = new Person();
		person.setFirstName("Maximo");
		person.setLastName("Cristaldo");
		c.set(1915, Calendar.OCTOBER, 28);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Morada do Sossego", "Campo Grande", "MS", "79200-001", country) );
		person.setPersonAddresses( pa );
		
		// Salvo um pessoa para depois pesquisar via API
		person = personService.save(person);
		
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/{id}", person.getId())			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Maximo"))
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.personAddresses[*]").isNotEmpty());
		
		// procura por um id q não existe
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/{id}", "id_q_nao_existe")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isNoContent());		
		
		destroyData();
	}
	
	@Test
	void whenReplace() throws Exception {
		createData();
		
		Person person;
		Calendar c = Calendar.getInstance();		
		Country country = new Country(null, "Brasil");
		mongoTemplate.save(country);
		
		person = new Person();
		person.setFirstName("Maximo");
		person.setLastName("Cristaldo");
		c.set(1915, Calendar.OCTOBER, 28);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Morada do Sossego", "Campo Grande", "MS", "79200-001", country) );
		person.setPersonAddresses( pa );
		
		// Salvo uma pessoa
		person = personService.save(person);
		
		
		// vou fazer uma alteração no nome da pessoa 
		person.setFirstName("Max");
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/REST/person/{id}", person.getId() )
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk());
		// verifica se alterou mesmo
		person = personService.findById(person.getId());
		assertEquals("Max", person.getFirstName());
		
		// se id null MethodNotAllowed
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/REST/person/{id}","")
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/REST/person")
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
		
		destroyData();
	}
	
	
	@Test
	void whenDelete() throws Exception {
		createData();
		
		Person person;
		Calendar c = Calendar.getInstance();		
		Country country = new Country(null, "Brasil");
		mongoTemplate.save(country);
		
		person = new Person();
		person.setFirstName("Maximo");
		person.setLastName("Cristaldo");
		c.set(1915, Calendar.OCTOBER, 28);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Morada do Sossego", "Campo Grande", "MS", "79200-001", country) );
		person.setPersonAddresses( pa );
		
		// Salvo uma pessoa
		person = personService.save(person);
		
		// deleta a pessoa salva usando API
		mockMvc.perform( MockMvcRequestBuilders
			      .delete("/REST/person/{id}", person.getId() )
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk());		
		// verifica se removeu
		try {
			person = personService.findById(person.getId());
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(PersonService.COULD_NOT_FIND+person.getId(), e.getMessage());
		}
		
		// tenta remover uma pessoa q não existe
		mockMvc.perform( MockMvcRequestBuilders
			      .delete("/REST/person/{id}", "id_q_nao_existe")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isNoContent());		
		
		destroyData();
	}
	
	@Test
	void whenFindByFirstName() throws Exception {
		createData();
		
		// procura todos q tenham a letra i no nome
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/firstname/{firstName}", "i")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Felipe"));
			      	
		
		// procura por um nome q não existe
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/firstname/{firstName}", "nome_q_nao_existe")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isNoContent());		
		
		
		destroyData();
	}

	@BeforeAll
	private void createData() {
	}
	
	@AfterAll
	private void destroyData() {		
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
