package kr.co.suitcarrier.web.controller;

import kr.co.suitcarrier.web.dto.PostCreateRequestDto;
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

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.test();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto requestDto) {
        return postService.createPost(requestDto);
    }
//    @GetMapping("/search")
//    public ResponseEntity<List<PostCardResponseDto>> deletePost(@RequestParam) {
//
//        return null;
//    }

}

