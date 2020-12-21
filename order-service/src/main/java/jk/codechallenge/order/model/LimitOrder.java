package jk.codechallenge.order.model;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class LimitOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long limitOrderId;
	private Double priceLimit;
	private Long accountId;
	private Double amount;
	private OrderStatus status;

	public LimitOrder() {
	}

	public LimitOrder(Double priceLimit, Long accountId, Double amount) {
		this.priceLimit = priceLimit;
		this.accountId = accountId;
		this.amount = amount;
	}

	public Long getLimitOrderId() {
		return limitOrderId;
	}

	public void setLimitOrderId(Long limitOrderId) {
		this.limitOrderId = limitOrderId;
	}

	public Double getPriceLimit() {
		return priceLimit;
	}

	public void setPriceLimit(Double priceLimit) {
		this.priceLimit = priceLimit;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LimitOrder that = (LimitOrder) o;
		return Objects.equals(limitOrderId, that.limitOrderId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(limitOrderId);
	}

	/**
	 * This is a method to calculate whether or not the total price is adequate for a given balance
	 * @param balance The Account balance
	 * @return True if the product of the (BTC) amount with the priceLimit is less or equal with the given {@param balance}
	 */
	public boolean isTotalBalanceAdequate(Double balance) {
		double total = this.getAmount() * this.getPriceLimit();
		return total <= balance;
	}
}
