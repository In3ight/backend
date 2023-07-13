package kr.co.suitcarrier.web.controller;

import kr.co.suitcarrier.web.dto.PostCreateRequestDto;
import kr.co.suitcarrier.web.dto.ReviewCreateRequestDto;
import kr.co.suitcarrier.web.dto.ReviewUpdateRequestDto;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto requestDto) {
        return postService.createPost(requestDto);
    }
//    @GetMapping("/search")
//    public ResponseEntity<List<PostCardResponseDto>> deletePost(@RequestParam) {
//
//        return null;
//    }

    @PostMapping("/{postId}/createReview")
    public ResponseEntity<?> createReview( @PathVariable("postId") Long postId, @RequestBody ReviewCreateRequestDto requestDto) {
        return postService.createReview(requestDto, postId);
    }

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

