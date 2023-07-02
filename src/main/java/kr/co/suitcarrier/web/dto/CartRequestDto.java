package kr.co.suitcarrier.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CartRequestDto {

    private Integer user;
    private Integer post;
    private LocalDateTime rentDate;
    private LocalDateTime returnDate;
    private int rentPossible;
    
}

