package jk.codechallenge.order.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
	PROCESSED(0),
	EXECUTED(1),
	REJECTED(2),
	CANCELED(3);

	private final int statusCode;

	OrderStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	@JsonValue
	public int getStatusCode() {
		return statusCode;
	}
}
