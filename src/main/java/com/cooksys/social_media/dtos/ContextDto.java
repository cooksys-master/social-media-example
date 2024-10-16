package com.cooksys.social_media.dtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContextDto {
	
	private TweetDto target;
	
	private List<TweetDto> before;
	
	private List<TweetDto> after;

}
