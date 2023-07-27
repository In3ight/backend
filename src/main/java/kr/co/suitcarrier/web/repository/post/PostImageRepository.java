package kr.co.suitcarrier.web.repository.post;

import kr.co.suitcarrier.web.entity.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    // 해당 포스트의 전체 이미지
    List<PostImage> findAllByPostId(Long id);
    // 해당하는 이미지 하나만
    Optional<PostImage> findTopByPostId(Long id);


}
