package kr.co.suitcarrier.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.suitcarrier.web.dto.LikeRequestDto;
import kr.co.suitcarrier.web.service.LikeService;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    LikeService likeService;

    // 찜 조회
    @GetMapping("/like/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(likeService.getLikeList(userId));
    }

    // 찜 추가하기
    @PostMapping("/like/create")
    public ResponseEntity<?> createLike(@RequestBody LikeRequestDto likeRequestDto) {
        try {
            Integer userId = likeRequestDto.getUser();
            Integer postId = likeRequestDto.getPost();

            // 찜 중복 확인
            if(likeService.getLikeList(userId, postId) == null) {
                throw new Exception("createLike request with the duplicated like.");
            }

            // 새로운 찜 생성
            likeService.createLike(userId, postId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    } 

    // 찜 제거하기
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLike(@RequestBody LikeRequestDto likeRequestDto) {
        try {
            Integer userId = likeRequestDto.getUser();
            Integer postId = likeRequestDto.getPost();

            // 찜 존재하는지 확인
            if(likeService.getLikeList(userId, postId) == null) {
                throw new Exception("deleteLike request with no corresponding like.");
            }

            // 찜 삭제
            likeService.deleteLike(userId, postId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } 
    }

}