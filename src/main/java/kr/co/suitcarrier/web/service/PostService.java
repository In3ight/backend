package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.repository.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostStateRepository postStateRepository;
    private final RentalDateRepository rentalDateRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createPost() {
        // dto return
        try {

        } catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
    }

    @Transactional
    public void getPostList() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
    }

}
