package jk.codechallenge.order.repository;

import jk.codechallenge.order.model.LimitOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LimitOrderRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private LimitOrderRepository limitOrderRepository;

	@Test
	public void fetchOrderDetails() {
		LimitOrder limitOrder = new LimitOrder(3100.0, 1L, 1.0);
		entityManager.persist(limitOrder);
		entityManager.flush();

		LimitOrder given = limitOrderRepository.findByLimitOrderId(1L);
		Assertions.assertEquals(3100.0, given.getPriceLimit());
	}
}
