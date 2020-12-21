package jk.codechallenge.order.model;

import java.util.Objects;

public class Account {

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
	 * This is a method that aggregates the {@param amount} to the existing btcBalance
	 * and subtracts the total price (i.e. {@param amount} * {@param priceLimit}) from the existing usdBalance
	 * @param amount The BTC amount entered with a Limit Order
	 * @param priceLimit The Price Limit entered with a Limit Order
	 */
	public void updateBalance(Double amount, Double priceLimit) {
		this.setBtcBalance(this.getBtcBalance() + amount);
		this.setUsdBalance(this.getUsdBalance() - (amount * priceLimit));
	}
}
