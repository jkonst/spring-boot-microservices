package jk.codechallenge.account.service;

import jk.codechallenge.account.model.Account;
import jk.codechallenge.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private AccountRepository repository;

	public Account saveAccount(Account account) {
		return repository.save(account);
	}

	public Account fetchAccountDetails(Long accountId) {
		return repository.findByAccountId(accountId);
	}

}
