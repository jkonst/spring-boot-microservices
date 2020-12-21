package jk.codechallenge.order.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLimitOrderException extends RuntimeException {
	public InvalidLimitOrderException(String message) {
		super(message);
	}
}
