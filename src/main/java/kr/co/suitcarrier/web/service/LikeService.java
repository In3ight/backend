package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.dto.LikeRequestDto;
import kr.co.suitcarrier.web.entity.Like;
import kr.co.suitcarrier.web.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

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
    public Optional<Like> getLikeList(Integer userId, Integer postId) {
        try {
            return likeRepository.findByUserAndPost(userId, postId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createLike(Integer userId, Integer postId) {
        try {
            Like likeEntity = new Like();
            likeEntity.setUser(userId);
            likeEntity.setPost(postId);
            likeRepository.save(likeEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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