package com.cooksys.social_media.dtos;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TweetDto {
	
	private Integer id;
	
	private UserResponseDto author;
	
	private Timestamp posted;
	
	private String content;
	
	private TweetDto inReplyTo;
	
	private TweetDto repostOf;

}
