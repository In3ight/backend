package kr.co.suitcarrier.web.controller;

import kr.co.suitcarrier.web.dto.PostCreateRequestDto;
import kr.co.suitcarrier.web.dto.ProductCreateRequestDto;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.entity.post.Post;
import kr.co.suitcarrier.web.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @PostMapping("create")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, ProductCreateRequestDto productCreateRequestDto, User user) {
        int postId = PostService.createPost(postCreateRequestDto, productCreateRequestDto, user.getUuid());
        return ResponseEntity.ok(postId);
    }

}

