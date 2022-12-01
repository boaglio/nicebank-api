package com.nicebank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest 
class NicebankApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	

	static final String URI_DEPOSIT ="/api/deposit";
	static final String URI_WITHDRAW ="/api/withdraw";

	@BeforeEach
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void testDeposit() throws Exception {
		
		this.mvc.perform(
			 post(URI_DEPOSIT)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"Machado de Assis\",\"accountId\":50001,\"value\":100}"))	
			.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testWithdraw() throws Exception {
 
		this.mvc.perform(
				 post(URI_WITHDRAW)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"username\":\"Machado de Assis\",\"accountId\":50001,\"value\":100}"))	
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void testWithdrawNoEnoughBalance() throws Exception {
 
		this.mvc.perform(
			 post(URI_WITHDRAW)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"Machado de Assis\",\"accountId\":50001,\"value\":99999999}"))	
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testWithdrawUserNotFound() throws Exception {
 
		this.mvc.perform(
			 post(URI_WITHDRAW)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"Boaglio\",\"accountId\":50001,\"value\":1}"))	
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testWithdrawBankAccountNotFound() throws Exception {
 
		this.mvc.perform(
			 post(URI_WITHDRAW)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"Machado de Assis\",\"accountId\":333333,\"value\":1}"))	
			.andExpect(status().is4xxClientError());
	}
	
}
