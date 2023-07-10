package kr.co.suitcarrier.web.dto;

import kr.co.suitcarrier.web.entity.Cart;

import java.util.List;

public class CartResponseDto {

    private List<Cart> cartList;

    public CartResponseDto(List<Cart> cartList) {
		this.cartList = cartList;
	}

}
