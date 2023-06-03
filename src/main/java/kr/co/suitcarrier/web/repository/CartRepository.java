package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    }
