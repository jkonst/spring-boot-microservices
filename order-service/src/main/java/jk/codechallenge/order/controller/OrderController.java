package jk.codechallenge.order.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jk.codechallenge.order.exceptions.InvalidLimitOrderException;
import jk.codechallenge.order.exceptions.LimitOrderNotFoundException;
import jk.codechallenge.order.exceptions.TooManyRequestsException;
import jk.codechallenge.order.model.LimitOrder;
import jk.codechallenge.order.service.LimitOrderService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/order")
public class OrderController {
	private final LimitOrderService limitOrderService;
	private final Bucket bucket;

	public OrderController(LimitOrderService limitOrderService) {
		this.limitOrderService = limitOrderService;
		Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
		this.bucket = Bucket4j.builder().addLimit(limit).build();
	}

	@GetMapping("/{id}")
	public LimitOrder fetchOrderDetails(@PathVariable("id") Long limitOrderId) {
		LimitOrder existingOrder = limitOrderService.fetchOrder(limitOrderId);
		if (existingOrder == null) {
			throw new LimitOrderNotFoundException("Order with id: " + limitOrderId + " does not exist");
		}
		return existingOrder;
	}

	@PostMapping("/")
	public LimitOrder createLimitOrder(@RequestBody LimitOrder order) {
		if (order.getPriceLimit() == null || order.getPriceLimit() <= 0) {
			throw new InvalidLimitOrderException("limit order with invalid price per BTC");
		}

		if (order.getAmount() == null || order.getAmount() <= 0) {
			throw new InvalidLimitOrderException("limit order with zero BTC amount");
		}

		if (order.getAccountId() == null || order.getAccountId() < 1) {
			throw new InvalidLimitOrderException("limit order with invalid account id");
		}

		if (bucket.tryConsume(1)) {
			return limitOrderService.createOrder(order);
		}
		throw new TooManyRequestsException("Too Many Requests");
	}
}
