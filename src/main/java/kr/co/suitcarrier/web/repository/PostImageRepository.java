package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {


}