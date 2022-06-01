package br.com.cjt.easybim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication (exclude = {DataSourceAutoConfiguration.class }) // = @Configuration @EnableAutoConfiguration @ComponentScan
@EnableMongoRepositories
//@EnableSpringDataWebSupport // testar para ver se precisa para a paginação
@EnableCaching
public class EasyBimApplication {

	public static void main(String[] args) {
		//SpringApplication springApplication = new SpringApplication(EasyBimApplication.class);
		//springApplication.addListeners(new CascadingMongoEventListener<>());
		//springApplication.run(args);
		SpringApplication.run(EasyBimApplication.class, args);
	}
}
