package kr.co.suitcarrier.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.suitcarrier.web.dto.post.*;
import kr.co.suitcarrier.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@CrossOrigin(origins = {"https://suitcarrier.co.kr", "http://localhost:3000"})
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    @Operation(summary = "게시글 생성")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPost(@ModelAttribute PostCreateRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @DeleteMapping("/{postId}/delete")
    @Operation(summary = "게시글 삭제")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        return postService.deletePost(postId);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세조회")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable("postId") Long postId) {
        return postService.getPost(postId);
    }

    @GetMapping("/list")
    @Operation(summary = "게시글 목록 조회(카드형)", description = "필요에 따라 여러가지 조회 방법 추가할 예정")
    public ResponseEntity<ListingResponseDto> listingPosts() {
        return postService.listingPosts();
    }

    @PostMapping("/search")
    @Operation(summary = "매물 검색", description = "size, color, brand의 경우, '선택 없음'->null로 처리할 것.")
    public ResponseEntity<SearchResponseDto> searchPosts(@RequestBody SearchRequestDto requestDto) {
        return postService.searchPosts(requestDto);
    }

    @PostMapping("/{postId}/createReview")
    @Operation(summary = "게시글 리뷰 작성")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReview( @PathVariable("postId") Long postId, @RequestBody ReviewCreateRequestDto requestDto) {
        return postService.createReview(requestDto, postId);
    }

    @PutMapping("/{reviewId}/updateReview")
    @Operation(summary = "게시글 리뷰 수정")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewUpdateRequestDto requestDto) {
        return postService.updateReview(requestDto, reviewId);
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.test();
        return ResponseEntity.ok().build();
    }

}

