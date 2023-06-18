package kr.co.suitcarrier.web.repository.post;

import kr.co.suitcarrier.web.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop5ByOrderByCreatedAtDesc();    // 임시 코드. 추천 알고리즘 새로 작성해야.


}
