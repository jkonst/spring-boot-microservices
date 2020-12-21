package jk.codechallenge.order.repository;

import jk.codechallenge.order.model.LimitOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitOrderRepository extends JpaRepository<LimitOrder, Long> {

	LimitOrder findByLimitOrderId(Long limitOrderId);
}
