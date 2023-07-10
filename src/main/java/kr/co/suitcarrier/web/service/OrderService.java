package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.entity.Order;
import kr.co.suitcarrier.web.entity.OrderState;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.repository.OrderRepository;
import kr.co.suitcarrier.web.repository.OrderStateRepository;
import kr.co.suitcarrier.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStateRepository orderStateRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Order> getOrderList(Integer userId) {
        try {
            return orderRepository.findByUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Transactional
    public Optional<OrderState> getOrderState(Integer orderStateId) {
        try {
            return orderStateRepository.findById(orderStateId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean createOrder(String userEmail, String finalPrice, LocalDateTime rentDate, LocalDateTime createdAt) {
        try {
            Order orderEntity = new Order();

            User user = userRepository.findByEmail(userEmail).get();
            orderEntity.setUser(user);

            orderEntity.setFinalPrice(finalPrice);
            orderEntity.setRentDate(rentDate);
            orderEntity.setCreatedAt(createdAt);
            orderEntity.setUpdatedAt(createdAt);
            orderEntity.setIsDeleted(0);

            orderRepository.save(orderEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}