package jk.codechallenge.order.service;

import jk.codechallenge.order.exceptions.InvalidLimitOrderException;
import jk.codechallenge.order.model.Account;
import jk.codechallenge.order.model.BtcCurrency;
import jk.codechallenge.order.model.LimitOrder;
import jk.codechallenge.order.model.OrderStatus;
import jk.codechallenge.order.repository.LimitOrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;

@RunWith(SpringRunner.class)
public class LimitOrderServiceTest {

	@Autowired
	private LimitOrderService limitOrderService;

	@TestConfiguration
	static class LimitOrderTestContextConfiguration {
		@Bean
		public LimitOrderService limitOrderService() {
			return new LimitOrderService();
		}
	}

	@MockBean
	private LimitOrderRepository limitOrderRepository;
	@MockBean
	private RestTemplate restTemplate;
	@MockBean
	private ScheduledExecutorService executor;

	@Test
	public void fetchOrderDetails() {
		LimitOrder limitOrder = new LimitOrder(3100.0, 1L, 1.0);
		Mockito.when(limitOrderRepository.findByLimitOrderId(1L)).thenReturn(limitOrder);
		LimitOrder given = limitOrderService.fetchOrder(1L);
		Assertions.assertEquals(limitOrder.getPriceLimit(), given.getPriceLimit());
		Assertions.assertEquals(limitOrder.getAmount(), given.getAmount());
		Assertions.assertEquals(limitOrder.getPriceLimit(), given.getPriceLimit());
	}

	@Test(expected = InvalidLimitOrderException.class)
	public void createOrderWithNonExistingId() {
		LimitOrder limitOrder = new LimitOrder(3100.0, 1L, 1.0);
		Mockito.when(restTemplate.getForObject(limitOrderService.GET_ACCOUNT_URL + limitOrder.getAccountId(),
				Account.class)).thenReturn(null);
		limitOrderService.createOrder(limitOrder);
	}

	@Test
	public void createRejectedOrder() {
		LimitOrder limitOrder = new LimitOrder(3100.0, 1L, 1.0);
		Account account = new Account("John Konstas", 300.12, 0.0);
		Mockito.when(restTemplate.getForObject(limitOrderService.GET_ACCOUNT_URL + limitOrder.getAccountId(),
				Account.class)).thenReturn(account);
		limitOrderService.createOrder(limitOrder);
		Assertions.assertEquals(OrderStatus.REJECTED, limitOrder.getStatus());
	}

	@Test
	public void createProcessedOrder() throws InterruptedException {
		LimitOrder limitOrder = new LimitOrder(3100.0, 1L, 1.0);
		Account account = new Account("John Konstas", 3200.12, 0.0);
		BtcCurrency btcCurrency = new BtcCurrency("2020-12-12T20:24:17.114910", 3000.0);
		Mockito.when(restTemplate.getForObject(limitOrderService.GET_ACCOUNT_URL + limitOrder.getAccountId(),
				Account.class)).thenReturn(account);
		Mockito.when(restTemplate.getForObject(limitOrderService.BTC_CURRENCY_URL, BtcCurrency.class))
				.thenReturn(btcCurrency);
		limitOrderService.createOrder(limitOrder);
		Assertions.assertEquals(OrderStatus.PROCESSED, limitOrder.getStatus());
	}
}
