package jk.codechallenge.account.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long accountId;
	private String name;
	private Double usdBalance;
	private Double btcBalance;

	public Account() {
	}

	public Account(String name, Double usdBalance, Double btcBalance) {
		this.name = name;
		this.usdBalance = usdBalance;
		this.btcBalance = btcBalance;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getUsdBalance() {
		return usdBalance;
	}

	public void setUsdBalance(Double usdBalance) {
		this.usdBalance = usdBalance;
	}

	public Double getBtcBalance() {
		return btcBalance;
	}

	public void setBtcBalance(Double btcBalance) {
		this.btcBalance = btcBalance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Account account = (Account) o;
		return accountId.equals(account.accountId) &&
				name.equals(account.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, name);
	}

	/**
	 * This is a method that updates properties of the current Account object with not null properties of another
	 * Account object, when the latter have different values from the ones of the current object.
	 * @param account The Account object whose properties are used to update the current Account object
	 */
	public void updateFromAccount(Account account) {
		String newName = account.getName();
		Double newUsdBalance = account.getUsdBalance();
		Double newBtcBalance = account.getBtcBalance();

		if (newName != null && !this.getName().equals(newName)) {
			this.setName(newName);
		}

		if (newBtcBalance != null && newBtcBalance != this.getBtcBalance().doubleValue()) {
			this.setBtcBalance(newBtcBalance);
		}

		if (newUsdBalance != null && newUsdBalance != this.getUsdBalance().doubleValue()) {
			this.setUsdBalance(newUsdBalance);
		}
	}

	/**
	 * This is a method to chech that there is at least a valid account id and another valid property
	 * @return True if the current Account object has a valid account id and at least one not-null property
	 */
	public boolean hasMinimumPropertiesToUpdate() {
		if (!hasValidAccountId()) {
			return false;
		}
		return this.getName() != null || this.getBtcBalance() != null || this.getUsdBalance() != null;
	}

	/**
	 * This is a method to check that there is at least a valid name property when
	 * creating an account object
	 * @return True if the current Account object has at least a not null and not empty name
	 */
	public boolean hasMinimumPropertiesToCreate() {
		return this.getName() != null && !this.getName().isEmpty();
	}

	/**
	 * This method converts null balances to 0.0
	 */
	public void balanceSanitize() {
		this.usdBalance = this.usdBalance == null ? 0.0 : this.usdBalance;
		this.btcBalance = this.btcBalance == null ? 0.0 : this.btcBalance;
	}

	/**
	 * This is a method to check validity of the current object's account id
	 * @return True if the current object has a not null positive account id
	 */
	private boolean hasValidAccountId() {
		return this.getAccountId() != null && this.getAccountId() >= 1;
	}
}
