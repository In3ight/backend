package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM order WHERE user_id = :userId)", nativeQuery = true)
    List<Order> findByUser(Integer userId);

}
