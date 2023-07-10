package kr.co.suitcarrier.web.dto;

import kr.co.suitcarrier.web.entity.Like;

import java.util.List;

public class LikeResponseDto {

    private List<Like> likeList;

    public LikeResponseDto(List<Like> likeList) {
		this.likeList = likeList;
	}

}
