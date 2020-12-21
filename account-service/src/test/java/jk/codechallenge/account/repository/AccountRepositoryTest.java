package jk.codechallenge.account.repository;

import jk.codechallenge.account.model.Account;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void fetchAccountDetails() {
		Account account = new Account("John Konstas", 3000.12, 0.0);
		entityManager.persist(account);
		entityManager.flush();

		Account requested = accountRepository.findByAccountId(1L);
		Assertions.assertEquals("John Konstas", requested.getName());
	}

}
