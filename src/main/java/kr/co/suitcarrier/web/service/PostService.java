package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.config.CustomUserDetails;
import kr.co.suitcarrier.web.dto.post.*;
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

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> deletePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUuid();
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + uuid));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postId));

        // 게시글 작성자와 요청한 유저 비교
        if(!post.getUser().getUuid().equals(uuid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없는 사용자입니다.");
        }

        // 게시글 논리삭제 (실제로 삭제되지 않지만, 논리삭제된 데이터는 모든 쿼리에서 사전 제외됨)
        post.updateIsDeleted(true);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<PostResponseDto> getPost(Long postid) {
        Post post = postRepository.findById(postid)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + postid));

        Product product = post.getProduct();
        PostState postState = post.getPostState();

        PostResponseDto responseDto = new PostResponseDto(post);
        return ResponseEntity.ok(responseDto);
    }

    @Transactional
    public ResponseEntity<?> createReview(ReviewCreateRequestDto requestDto, Long postId) {
        // 대여 이력이 있는 유저만 리뷰 작성 가능해야됨. 추후 로직 추가할 것
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
                    .status(HttpStatus.NOT_FOUND)
                    .body(postId + " 게시물이 존재하지 않음");
        }

    }

    @Transactional
    public ResponseEntity<?> updateReview(ReviewUpdateRequestDto requestDto, Long reviewId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUuid();
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + uuid));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다. id=" + reviewId));

        // 리뷰 작성자와 요청한 유저 비교
        if(!review.getUser().getUuid().equals(uuid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없는 사용자입니다.");
        }

        review.update(requestDto.getContent());

        return ResponseEntity.ok(reviewId);
    }

    @Transactional
    public ResponseEntity<SearchResponseDto> searchPosts(SearchRequestDto requestDto) {
//        private double longitude;
//        private double latitude;

//        color, brand, size에 null 입력 시 해당 조건은 무시됨.
//        예를 들어 color에 null 값이 들어오면 모든 색상의 게시글이 조회됨

        List<PostCardResponseDto> dtos = postRepository.findByConditions(requestDto.getColor(), requestDto.getBrand(), requestDto.getSize(),
                        requestDto.getMinPrice(), requestDto.getMaxPrice())
                .stream()
                .map(post -> PostCardResponseDto.builder().post(post).build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(SearchResponseDto.builder().postCardResponseDtos(dtos).build());
    }

    @Transactional
    public ResponseEntity<ListingResponseDto> listingPosts() {
        // 최신순으로 게시글 가져오기
        List<PostCardResponseDto> dtos = postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> PostCardResponseDto.builder().post(post).build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ListingResponseDto.builder().postCardResponseDtos(dtos).build());
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


}
