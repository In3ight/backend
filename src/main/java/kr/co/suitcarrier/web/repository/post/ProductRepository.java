package kr.co.suitcarrier.web.repository.post;


import kr.co.suitcarrier.web.entity.post.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


}