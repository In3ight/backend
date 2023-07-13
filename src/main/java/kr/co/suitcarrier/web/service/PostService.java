package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.config.CustomUserDetails;
import kr.co.suitcarrier.web.dto.PostCreateRequestDto;
import kr.co.suitcarrier.web.dto.ReviewCreateRequestDto;
import kr.co.suitcarrier.web.dto.ReviewUpdateRequestDto;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.entity.post.PostState;
import kr.co.suitcarrier.web.entity.post.Product;
import kr.co.suitcarrier.web.entity.post.Review;
import kr.co.suitcarrier.web.repository.ReviewRepository;
import kr.co.suitcarrier.web.repository.UserRepository;
import kr.co.suitcarrier.web.repository.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostStateRepository postStateRepository;
    private final RentalDateRepository rentalDateRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ResponseEntity<?> createPost(PostCreateRequestDto requestDto) {
        // 유저정보 조회>입력된 정보를 바탕으로 상품 생성->게시글상태 생성->게시글 생성
        User user;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUuid();

        if(uuid != null) {
            user = userRepository.findByUuid(uuid).get();
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("현재 사용자의 uuid가 존재하지 않음");
        }

        Long postId = null;

        if(user != null) {
            try {
                Product product = Product.builder()
                        .color(requestDto.getColor())
                        .name(requestDto.getName())
                        .size(requestDto.getSize())
                        .brand(requestDto.getBrand())
                        .build();
                product = productRepository.save(product);

                PostState postState = postStateRepository.save(PostState.builder().state(PostState.State.LENT_POSSIBLE).build());

                Post post = Post.builder()
                        .title(requestDto.getTitle())
                        .description(requestDto.getDescription())
                        .price(requestDto.getPrice())
                        .additionalPrice(requestDto.getAdditionalPrice())
                        .user(user)
                        .postState(postState)
                        .product(product)
                        .build();

                System.out.println("게시글:"+post.getId()+post.getTitle());
                postRepository.save(post);
                postId = post.getId();

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("게시글 생성 실패");
            }
        }
        return ResponseEntity.ok(postId);
    }

    @Transactional
    public void test() {
        User user;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUuid();

        System.out.println("테스트"+uuid+" "+uuid.getClass().getTypeName());
        if(uuid != null) {
            user = userRepository.findByUuid(uuid).get();
        } else {
        }
    }

    @Transactional
    public void getPostList() {
    }

    @Transactional
    public ResponseEntity<?> createReview(ReviewCreateRequestDto requestDto, Long postId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String uuid = userDetails.getUuid();

            User user = userRepository.findByUuid(uuid).get();
            Post post = postRepository.findById(postId).get();
            if (post != null) {
                Long reviewId = reviewRepository.save(requestDto.toEntity(user, post)).getId();
                return ResponseEntity.ok(reviewId);
            } else {
                System.out.println("아이디:"+postId);
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(postId + " 게시물이 존재하지 않음");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("리뷰 생성 실패");
        }

    }

    @Transactional
    public ResponseEntity<?> updateReview(ReviewUpdateRequestDto requestDto, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다. id=" + reviewId));
        review.update(requestDto.getContent());

        return ResponseEntity.ok(reviewId);
    }
}
