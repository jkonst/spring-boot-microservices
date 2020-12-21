package jk.codechallenge.account.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jk.codechallenge.account.exceptions.AccountNotFoundException;
import jk.codechallenge.account.model.Account;
import jk.codechallenge.account.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.Is;

import java.io.IOException;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private AccountService accountService;

	@Test
	public void fetchAccountDetails() throws Exception {
		Account account = new Account("John Konstas", 3000.12, 0.0);
		given(accountService.fetchAccountDetails(1L)).willReturn(account);

		mvc.perform(get("/account/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", Is.is(account.getName())));
	}

	@Test
	public void fetchNotFoundAccountDetails() throws Exception{
		given(accountService.fetchAccountDetails(12L)).willReturn(null);
		mvc.perform(get("/account/12")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createAccount() throws Exception {
		Account account = new Account("John Konstas", 3000.12, 0.0);
		account.setAccountId(1L);
		given(accountService.saveAccount(account)).willReturn(account);
		mvc.perform(post("/account/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonBytes(account)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Konstas"));
	}

	@Test
	public void updateAccount() throws Exception {
		Account account = new Account("John Konstas", 0.12, 1.0);
		account.setAccountId(1L);
		given(accountService.fetchAccountDetails(account.getAccountId())).willReturn(account);
		given(accountService.saveAccount(account)).willReturn(account);
		mvc.perform(put("/account/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonBytes(account)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Konstas"))
				.andExpect(jsonPath("$.btcBalance").value(1.0));
	}

	private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
}
