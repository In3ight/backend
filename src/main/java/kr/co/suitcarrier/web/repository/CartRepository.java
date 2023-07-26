package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUserId(Long userId);

    @Query(value = "SELECT * " +
                    "FROM cart " +
                    "WHERE post_id = :postId" +
                    "AND (" + 
                        "(:rentDate BETWEEN rent_date AND return_date)" +
                        "OR (:returnDate BETWEEN rent_date AND return_date)" +
                    ")", 
                    nativeQuery = true)
    Optional<Cart> findByPostAndDateRange(Long postId, LocalDateTime rentDate, LocalDateTime returnDate);

    void deleteByUuid(Long uuid);

}
