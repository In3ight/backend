package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.dto.CartRequestDto;
import kr.co.suitcarrier.web.entity.Cart;
import kr.co.suitcarrier.web.repository.CartRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    @Transactional
    public List<Cart> getCartList(Integer userId) {
        try {
            return cartRepository.findByUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public boolean canAddCart(Integer postId, LocalDateTime rentDate, LocalDateTime returnDate) {
        try {
            return (cartRepository.findByPostAndDateRange(postId, rentDate, returnDate) == null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    } 

    @Transactional
    public boolean createCartItem(
        Integer userId, 
        Integer postId, 
        LocalDateTime rentDate, 
        LocalDateTime returnDate, 
        Integer rentPossible
    ) {
        try {
            Cart cartEntity = new Cart();
            cartEntity.setUser(userId);
            cartEntity.setPost(postId);
            cartEntity.setRentDate(rentDate);
            cartEntity.setReturnDate(returnDate);
            cartEntity.setRentPossible(rentPossible);
            cartRepository.save(cartEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deleteCartItem(Integer cartId) {
        try {
            cartRepository.deleteById(cartId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}