package br.com.cjt.easybim;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Execution(ExecutionMode.CONCURRENT)
@TestInstance(value = Lifecycle.PER_CLASS)
class TabelaCustosIndicesImportTests {

	@Autowired
	MongoTemplate mongoTemplate;	 
		
	@Autowired
	MockMvc mockMvc;
	
	static volatile boolean threadBlocked = true;
	
	@Test
	void whenSave1() throws Exception {
		
		ClassPathResource res = new ClassPathResource("test/SINAPI_202202_Desonerado.xls");
		File file = res.getFile();
		FileInputStream fi1 = new FileInputStream(file);
		
        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", file.getName(), "text/plain", fi1);

        mockMvc.perform( MockMvcRequestBuilders.multipart("/REST/tabelacustosindices").file(mockMultipartFile))
        	.andDo(MockMvcResultHandlers.print())
        	.andDo(new ResultHandler() {				
				@Override
				public void handle(MvcResult result) throws Exception {
					// desbloqueio a Thread, sei que já incluí meu registro e os outros testes devem dar conflict.					
					threadBlocked = false;												
				}
			})
        	.andExpect(MockMvcResultMatchers.status().isOk());

		
		res = new ClassPathResource("test/SINAPI_202202_Desonerado.xls");
		file = res.getFile();
		fi1 = new FileInputStream(file);
		
        mockMultipartFile = new MockMultipartFile("user-file", file.getName(), "text/plain", fi1);

        mockMvc.perform( MockMvcRequestBuilders.multipart("/REST/tabelacustosindices").file(mockMultipartFile))
        	.andDo(MockMvcResultHandlers.print())
        	.andDo(new ResultHandler() {				
				@Override
				public void handle(MvcResult result) throws Exception {
					// desbloqueio a Thread, sei que já incluí meu registro e os outros testes devem dar conflict.					
					threadBlocked = false;												
				}
			})
        	.andExpect(MockMvcResultMatchers.status().isOk());
        
        mockMvc.perform( MockMvcRequestBuilders.multipart("/REST/tabelacustosindices").file(mockMultipartFile))
    		.andDo(MockMvcResultHandlers.print())
    		.andExpect(MockMvcResultMatchers.status().isConflict());
	}
	
	@Test
	void whenSave2() throws Exception {
		
		ClassPathResource res = new ClassPathResource("test/SINAPI_202112_Desonerado.xls");
		File file = res.getFile();
		FileInputStream fi1 = new FileInputStream(file);
		
		MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", file.getName(), "text/plain", fi1);
		
		// bloqueio a Thread até que eu posso começar o meu teste
		synchronized(this) {
	        while(threadBlocked) {	        	
	        	this.wait(10);	            
	        }
	    }
		
        mockMvc.perform( MockMvcRequestBuilders.multipart("/REST/tabelacustosindices").file(mockMultipartFile))
        	.andDo(MockMvcResultHandlers.print())
        	.andExpect(MockMvcResultMatchers.status().isConflict());
        
        mockMvc.perform( MockMvcRequestBuilders.multipart("/REST/tabelacustosindices").file(mockMultipartFile))
    		.andDo(MockMvcResultHandlers.print())
    		.andExpect(MockMvcResultMatchers.status().isConflict());
	}

	@BeforeAll
	private void createData() {
		//fail("Not yet implemented");
	}
	
	@AfterAll
	private void destroyData() {		
	}
}
