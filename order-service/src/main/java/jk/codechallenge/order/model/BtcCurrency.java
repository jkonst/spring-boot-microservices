package jk.codechallenge.order.model;

public class BtcCurrency {
	private String timestamp;
	private Double price;

	public BtcCurrency(String timestamp, Double price) {
		this.timestamp = timestamp;
		this.price = price;
	}

	public BtcCurrency() {
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
