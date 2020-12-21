package jk.codechallenge.account.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jk.codechallenge.account.exceptions.AccountNotFoundException;
import jk.codechallenge.account.exceptions.InvalidAccountException;
import jk.codechallenge.account.exceptions.TooManyRequestsException;
import jk.codechallenge.account.model.Account;
import jk.codechallenge.account.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;
	private final Bucket bucket;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
		Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
		this.bucket = Bucket4j.builder().addLimit(limit).build();
	}

	@GetMapping("/{id}")
	public Account fetchAccountDetails(@PathVariable("id") Long accountId) {
		Account existingAccount = accountService.fetchAccountDetails(accountId);
		if (existingAccount == null) {
			throw new AccountNotFoundException("Account with id: " + accountId + " does not exist");
		}

		return existingAccount;
	}

	@PutMapping("/")
	public Account updateAccount(@RequestBody Account account) {
		if (!account.hasMinimumPropertiesToUpdate()) {
			throw new InvalidAccountException("You tried to update an account with invalid id or null properties");
		}

		if (account.getName() != null && account.getName().isEmpty()) {
			throw new InvalidAccountException("You tried to update an account with empty name");
		}

		Account existingAccount = accountService.fetchAccountDetails(account.getAccountId());
		if (existingAccount == null) {
			throw new AccountNotFoundException("You tried to update an account with invalid or non-existing id");
		}

		existingAccount.updateFromAccount(account);
		return accountService.saveAccount(existingAccount);
	}

	@PostMapping("/")
	public Account createAccount(@RequestBody Account account) {
		if (!account.hasMinimumPropertiesToCreate()) {
			throw new InvalidAccountException("You tried to create an account with invalid accountId or name");
		}

		if (bucket.tryConsume(1)) {
			account.balanceSanitize();
			return accountService.saveAccount(account);
		}
		throw new TooManyRequestsException("Too Many Requests");
	}
}
