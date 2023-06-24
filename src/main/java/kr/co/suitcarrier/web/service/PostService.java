package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.dto.PostCreateRequestDto;
import kr.co.suitcarrier.web.dto.ProductCreateRequestDto;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.entity.post.Product;
import kr.co.suitcarrier.web.repository.UserRepository;
import kr.co.suitcarrier.web.repository.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostStateRepository postStateRepository;
    private final RentalDateRepository rentalDateRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post createPost(PostCreateRequestDto postCreateRequestDto, ProductCreateRequestDto productCreateRequestDto, String uuid) {
        // 유저정보 조회>입력된 정보를 바탕으로 상품 조회 및 생성->게시글상태 생성->게시글 생성
        try {
            Optional<User> user = userRepository.findByUuid(UUID.fromString(uuid));

//            Product product = productRepository.save(productCreateRequestDto.toEntity())

            return postRepository.save(postCreateRequestDto.toEntity(user.get(), )).getId();
        } catch (Exception e) {
            e.printStackTrace();
          return null;
        }
    }

    @Transactional
    public void getPostList() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
    }

}
