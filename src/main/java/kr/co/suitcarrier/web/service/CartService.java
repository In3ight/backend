package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.entity.Cart;
import kr.co.suitcarrier.web.entity.Post;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.repository.CartRepository;
import kr.co.suitcarrier.web.repository.PostRepository;
import kr.co.suitcarrier.web.repository.UserRepository;

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
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Cart> getCartList(Long userId) {
        try {
            return cartRepository.findByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public boolean canAddCart(Long postId, LocalDateTime rentDate, LocalDateTime returnDate) {
        try {
            return (cartRepository.findByPostAndDateRange(postId, rentDate, returnDate) == null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    } 

    // @Transactional
    // public boolean createCartItem(
    //     String userEmail, 
    //     Long postId,
    //     LocalDateTime rentDate, 
    //     LocalDateTime returnDate, 
    //     Integer rentPossible
    // ) {
    //     try {
    //         Cart cartEntity = new Cart();

    //         User user = userRepository.findByEmail(userEmail).get();
    //         cartEntity.setUser(user);
            
    //         Post post = postRepository.findById(postId);
    //         cartEntity.setPost(post);

    //         cartEntity.setRentDate(rentDate);
    //         cartEntity.setReturnDate(returnDate);
    //         cartEntity.setRentPossible(rentPossible);
    //         cartRepository.save(cartEntity);

    //         return true;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return false;
    //     }
    // }

    @Transactional
    public boolean deleteCartItem(Long cartUuid) {
        try {
            cartRepository.deleteByUuid(cartUuid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
