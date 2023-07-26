package kr.co.suitcarrier.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CartRequestDto {

    private Long user;
    private Long post;
    private LocalDateTime rentDate;
    private LocalDateTime returnDate;
    private int rentPossible;
    
}

