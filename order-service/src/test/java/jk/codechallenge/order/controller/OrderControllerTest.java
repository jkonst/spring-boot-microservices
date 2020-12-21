package jk.codechallenge.order.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jk.codechallenge.order.model.LimitOrder;
import jk.codechallenge.order.service.LimitOrderService;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private LimitOrderService limitOrderService;

	@Test
	public void fetchOrderDetails() throws Exception {
		LimitOrder limitOrder = new LimitOrder(3100.0, 1L, 1.0);
		given(limitOrderService.fetchOrder(1L)).willReturn(limitOrder);

		mvc.perform(get("/order/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.priceLimit", Is.is(limitOrder.getPriceLimit())));
	}

	@Test
	public void fetchNotFoundOrderDetails() throws Exception {
		given(limitOrderService.fetchOrder(12L)).willReturn(null);
		mvc.perform(get("/order/12")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createLimitOrder() throws Exception {
		LimitOrder limitOrder = new LimitOrder(3100.0, 1L, 1.0);
		limitOrder.setLimitOrderId(1L);
		given(limitOrderService.createOrder(limitOrder)).willReturn(limitOrder);
		mvc.perform(post("/order/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonBytes(limitOrder)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.limitOrderId").value(1))
				.andExpect(jsonPath("$.amount").value(1.0))
				.andExpect(jsonPath("$.accountId").value(1))
				.andExpect(jsonPath("$.priceLimit").value(3100.0));
	}

	private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
}
