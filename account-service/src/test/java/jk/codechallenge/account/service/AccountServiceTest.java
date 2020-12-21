package jk.codechallenge.account.service;

import jk.codechallenge.account.model.Account;
import jk.codechallenge.account.repository.AccountRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

	@Autowired
	private AccountService accountService;

	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {

		@Bean
		public AccountService accountService() {
			return new AccountService();
		}

	}

	@MockBean
	private AccountRepository accountRepository;

	@Test
	public void fetchAccoundDetails() {
		Account account = new Account("John Konstas", 3000.12, 0.0);
		Mockito.when(accountRepository.findByAccountId(1L)).thenReturn(account);
		Account requested = accountService.fetchAccountDetails(1L);
		Assertions.assertEquals("John Konstas", requested.getName());
	}

	@Test
	public void createAccount() {
		Account account = new Account("Yannis Konstas", 1000.02, 0.0);
		Mockito.when(accountRepository.save(account)).thenReturn(account);
		Account requested = accountService.saveAccount(account);
		Assertions.assertEquals("Yannis Konstas", requested.getName());
	}

}
