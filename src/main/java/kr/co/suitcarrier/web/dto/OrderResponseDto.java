package kr.co.suitcarrier.web.dto;

import kr.co.suitcarrier.web.entity.Order;

import java.util.List;

public class OrderResponseDto {

    private List<Order> orderList;

    public OrderResponseDto(List<Order> orderList) {
		this.orderList = orderList;
	}

}
