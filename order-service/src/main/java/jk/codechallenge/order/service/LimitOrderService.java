package jk.codechallenge.order.service;

import jk.codechallenge.order.exceptions.InvalidLimitOrderException;
import jk.codechallenge.order.model.Account;
import jk.codechallenge.order.model.BtcCurrency;
import jk.codechallenge.order.model.LimitOrder;
import jk.codechallenge.order.model.OrderStatus;
import jk.codechallenge.order.repository.LimitOrderRepository;
import jk.codechallenge.order.tasks.BtcCurrencyPoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class LimitOrderService {
	public final String BTC_CURRENCY_URL = "http://BTC-SERVICE/btcCurrency/";
	@Autowired
	private LimitOrderRepository repository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ScheduledExecutorService executor;
	public final String GET_ACCOUNT_URL = "http://ACCOUNT-SERVICE/account/";
	private final int POLL_TIMEOUT = 5;

	public LimitOrder createOrder(LimitOrder order) throws InvalidLimitOrderException {
		order.setStatus(OrderStatus.PROCESSED);
		Account orderAccount = null;
		try {
			orderAccount = restTemplate.getForObject(GET_ACCOUNT_URL + order.getAccountId(), Account.class);
		} catch (Exception ignored) {
		}
		if (orderAccount == null) {
			throw new InvalidLimitOrderException("The account id: " + order.getAccountId() + " does not exist");
		}
		// check if the account balance is adequate
		if (order.isTotalBalanceAdequate(orderAccount.getUsdBalance())) {
			// trigger asynchronous btc currency check
			pollBTCCurrency(order);
		} else {
			order.setStatus(OrderStatus.REJECTED);
		}
		return this.repository.save(order);
	}

	public LimitOrder fetchOrder(Long limitOrderId) {
		return this.repository.findByLimitOrderId(limitOrderId);
	}

	private void updateOrderStatus(LimitOrder order, OrderStatus status) {
		order.setStatus(status);
		this.repository.save(order);
	}

	/**
	 * This is a method that implements the business logic of the BtcCurrencyPoller.
	 * 1. Polls the btc currency service to fetch the BTC currency
	 * 2. Checks if the BTC currency limit is lower or equal with the {@param order} priceLimit
	 * 2a. If the above condition holds, it checks again the balance of the order Account because it may be different
	 * and if continues to be adequate, it updates both the account with the new balance and the order status as
	 * executed. Otherwise, it makes the order status rejected.
	 * Finally it cancels polling
	 * 2b. Else it repeats polling
	 *
	 * @param order The LimitOrder object that was processed
	 * @return new BtcCurrencyRunnable instance
	 */
	private BtcCurrencyPoller createBtcCurrencyRunnable(LimitOrder order) {
		return new BtcCurrencyPoller() {
			@Override
			public void run() {
				BtcCurrency btcCurrency = null;
				try {
					btcCurrency = restTemplate.getForObject(BTC_CURRENCY_URL,
							BtcCurrency.class);
				} catch (Exception ignored) {
				}
				if (btcCurrency != null && btcCurrency.getPrice() <= order.getPriceLimit()) {
					Account orderAccount = null;
					try {
						orderAccount = restTemplate.getForObject(GET_ACCOUNT_URL + order.getAccountId(),
								Account.class);
					} catch (Exception ignored) {
					}
					// before execution confirm that account balance is adequate
					if (orderAccount != null && order.isTotalBalanceAdequate(orderAccount.getUsdBalance())) {
						orderAccount.updateBalance(order.getAmount(), order.getPriceLimit());
						restTemplate.put(GET_ACCOUNT_URL, orderAccount);
						updateOrderStatus(order, OrderStatus.EXECUTED);
					} else {
						updateOrderStatus(order, OrderStatus.REJECTED);
					}
					this.getFuture().cancel(true);
				}
			}
		};
	}

	/**
	 * This is a method that polls btc currency service every 100 ms (this is because btc currency updates every 200 ms)
	 * for maximum {@param POLL_TIMEOUT} minutes.
	 *
	 * @param order The LimitOrder object that was processed
	 */
	private void pollBTCCurrency(LimitOrder order) {
		BtcCurrencyPoller runnable = createBtcCurrencyRunnable(order);
		Future<?> future = executor.scheduleAtFixedRate(runnable, 0, 100, TimeUnit.MILLISECONDS);
		runnable.setFuture(future);
		executor.schedule(() -> {
			System.out.println("scheduler ready to cancel");
			if (!future.isCancelled() && !future.isDone()) {
				System.out.println("future is not cancelled yet");
				updateOrderStatus(order, OrderStatus.CANCELED);
				future.cancel(true);
			}
		}, POLL_TIMEOUT, TimeUnit.MINUTES);
	}

}
