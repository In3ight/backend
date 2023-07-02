package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.OrderState;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStateRepository extends JpaRepository<OrderState, Long> {

    @Query(value = "SELECT * FROM order_state WHERE id = :orderStateId)", nativeQuery = true)
    Optional<OrderState> findById(Integer orderStateId);

}
