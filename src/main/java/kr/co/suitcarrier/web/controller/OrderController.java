package kr.co.suitcarrier.web.controller;

import kr.co.suitcarrier.web.config.CustomUserDetails;
import kr.co.suitcarrier.web.dto.CartResponseDto;
import kr.co.suitcarrier.web.dto.CartRequestDto;
import kr.co.suitcarrier.web.dto.CartDeleteRequestDto;
import kr.co.suitcarrier.web.dto.LikeResponseDto;
import kr.co.suitcarrier.web.dto.LikeRequestDto;
import kr.co.suitcarrier.web.service.CartService;
import kr.co.suitcarrier.web.service.LikeService;
import kr.co.suitcarrier.web.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    LikeService likeService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    // 찜 조회
    @GetMapping("/likes")
    public ResponseEntity<?> listLikeByUser() {
        // SecurityContextHolder 현재 로그인 중인 유저의 아아디를 가져온다
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
        CustomUserDetails userDetails = (CustomUserDetails)principal; 
        Integer userId = Integer.parseInt(userDetails.getId());

        // ResponseDTO를 만들어서 클라이언트에게 전달
        LikeResponseDto likeResponseDto = new LikeResponseDto(likeService.getLikeList(userId)); 
        return ResponseEntity.ok(likeResponseDto);
    }

    // 찜 추가하기
    @PostMapping("/likes")
    public ResponseEntity<?> createLike(@RequestBody LikeRequestDto likeRequestDto) {
        try {
            // SecurityContextHolder 현재 로그인 중인 유저 정보를 가져온다
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
            CustomUserDetails userDetails = (CustomUserDetails)principal; 
            Integer userId = Integer.parseInt(userDetails.getId());
            String userEmail = userDetails.getEmail(); 

            //DTO에서 postId를 가져온다
            Integer postId = likeRequestDto.getPost();

            // 찜 중복 확인
            if(likeService.getLike(userId, postId) != null) {
                throw new Exception("createLike request with the duplicated like.");
            }

            // 새로운 찜 생성
            likeService.createLike(userEmail, postId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    } 

    // 찜 제거하기
    @DeleteMapping("/likes")
    public ResponseEntity<?> deleteLike(@RequestBody LikeRequestDto likeRequestDto) {
        try {
            // SecurityContextHolder 현재 로그인 중인 유저 정보를 가져온다
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
            CustomUserDetails userDetails = (CustomUserDetails)principal; 
            Integer userId = Integer.parseInt(userDetails.getId());

            // DTO에서 postId를 가져온다
            Integer postId = likeRequestDto.getPost();

            // 찜 존재하는지 확인
            if(likeService.getLike(userId, postId) == null) {
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

    // 장바구니 조회
    @GetMapping("/cart")
    public ResponseEntity<?> listCartByUser() {
        // SecurityContextHolder 현재 로그인 중인 유저의 아아디를 가져온다
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
        CustomUserDetails userDetails = (CustomUserDetails)principal; 
        Integer userId = Integer.parseInt(userDetails.getId());

        // ResponseDTO를 만들어서 클라이언트에게 전달
        CartResponseDto cartResponseDto = new CartResponseDto(cartService.getCartList(userId)); 
        return ResponseEntity.ok(cartResponseDto);
    }

    // 장바구니 담기
    @PostMapping("/cart")
    public ResponseEntity<?> addCart(@RequestBody CartRequestDto cartRequestDto) {
        try {
            // SecurityContextHolder 현재 로그인 중인 유저의 정보를 가져온다
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
            CustomUserDetails userDetails = (CustomUserDetails)principal; 
            String userEmail = userDetails.getEmail(); 

            Integer postId = cartRequestDto.getPost();
            LocalDateTime rentDate = cartRequestDto.getRentDate();
            LocalDateTime returnDate = cartRequestDto.getReturnDate();
            Integer rentPossible = cartRequestDto.getRentPossible();

            // 장바구니 중복 처리
            if(cartService.canAddCart(postId, rentDate, returnDate) == false) {
                throw new Exception("addCart : Already existing cart item");
            }

            // 새로운 찜 생성
            cartService.createCartItem(userEmail, postId, rentDate, returnDate, rentPossible);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    } 

    // 장바구니 삭제
    @DeleteMapping("/cart")
    public ResponseEntity<?> removeCart(@RequestBody CartDeleteRequestDto cartRequestDto) {
        try {
            Integer cartId = cartRequestDto.getId();

            // 장바구니 삭제
            cartService.deleteCartItem(cartId);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } 
    }

    // 주문기록 조회
    @GetMapping("/history")
    public ResponseEntity<?> listOrderByUser() {
        // SecurityContextHolder 현재 로그인 중인 유저의 아아디를 가져온다
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
        CustomUserDetails userDetails = (CustomUserDetails)principal; 
        Integer userId = Integer.parseInt(userDetails.getId());
        
        return ResponseEntity.ok(orderService.getOrderList(userId));
    }

    // 주문상태 확인
    @GetMapping("/state/{orderStateId}")
    public ResponseEntity<?> getOrderState(@PathVariable Integer orderStateId) {
        return ResponseEntity.ok(orderService.getOrderState(orderStateId));
    }

}