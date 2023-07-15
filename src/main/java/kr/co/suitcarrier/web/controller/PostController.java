package kr.co.suitcarrier.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.suitcarrier.web.dto.*;
import kr.co.suitcarrier.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    // 게시글 상세조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable("postId") Long postid) {
        return postService.getPost(postid);
    }

    // 매물 검색
    @PostMapping("/search")
    @Operation(summary = "매물 검색", description = "size, color, brand의 경우, '선택 없음'->null로 처리할 것.")
    public ResponseEntity<SearchResponseDto> searchPosts(@RequestBody SearchRequestDto requestDto) {
        return postService.searchPosts(requestDto);
    }

    // 게시글 리뷰 작성
    @PostMapping("/{postId}/createReview")
    public ResponseEntity<?> createReview( @PathVariable("postId") Long postId, @RequestBody ReviewCreateRequestDto requestDto) {
        return postService.createReview(requestDto, postId);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}/updateReview")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewUpdateRequestDto requestDto) {
        return postService.updateReview(requestDto, reviewId);
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.test();
        return ResponseEntity.ok().build();
    }

}

