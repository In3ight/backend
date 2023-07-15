package kr.co.suitcarrier.web.repository.post;

import io.lettuce.core.dynamic.annotation.Param;
import kr.co.suitcarrier.web.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop5ByOrderByCreatedAtDesc();    // 임시 코드. 추천 알고리즘 새로 작성해야.

    // 다중조건 검색
    @Query("select p from Post p where (:color is null or p.product.color = :color) and (:brand is null or p.product.brand = :brand)" +
            "and (:size is null or p.product.size = : size) and p.price >= :minPrice and p.price <= :maxPrice")
    List<Post> findByConditions(@Param("color") String color, @Param("brand") String brand, @Param("size") String size,
                                @Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);


}
