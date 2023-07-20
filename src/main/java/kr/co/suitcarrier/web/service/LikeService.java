package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.dto.LikeRequestDto;
import kr.co.suitcarrier.web.entity.Like;
import kr.co.suitcarrier.web.entity.Post;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.repository.LikeRepository;
import kr.co.suitcarrier.web.repository.PostRepository;
import kr.co.suitcarrier.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Like> getLikeList(Integer userId) {
        try {
            return likeRepository.findByUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public Optional<Like> getLike(Integer userId, Integer postId) {
        try {
            return likeRepository.findByUserAndPost(userId, postId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // @Transactional
    // public boolean createLike(String userEmail, Integer postId) {
    //     try {
    //         Like likeEntity = new Like();

    //         User user = userRepository.findByEmail(userEmail).get();
    //         likeEntity.setUser(user);

    //         Post post = postRepository.findById(postId);
    //         likeEntity.setPost(post);

    //         likeRepository.save(likeEntity);
    //         return true;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return false;
    //     }
    // }

    @Transactional
    public boolean deleteLike(Integer userId, Integer postId) {
        try {
            likeRepository.deleteByUserAndPost(userId, postId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}