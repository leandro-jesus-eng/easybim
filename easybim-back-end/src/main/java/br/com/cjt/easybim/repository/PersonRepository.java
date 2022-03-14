package br.com.cjt.easybim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.com.cjt.easybim.data.Person;

public interface PersonRepository extends MongoRepository<Person, String>, EntityRepository<Person> {
	
	@Query("{ 'firstName' : { $regex : \"?0\"} }") // q tenha a string em algum lugar do nome	 
	public Optional<List<Person>> findByFirstName(String firstName);
}
