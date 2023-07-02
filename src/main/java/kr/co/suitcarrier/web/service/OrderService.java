package kr.co.suitcarrier.web.service;

import kr.co.suitcarrier.web.entity.Order;
import kr.co.suitcarrier.web.entity.OrderState;
import kr.co.suitcarrier.web.repository.OrderRepository;
import kr.co.suitcarrier.web.repository.OrderStateRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStateRepository orderStateRepository;

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

}