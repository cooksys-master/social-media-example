package com.cooksys.social_media.dtos;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HashtagDto {
	
	private String label;
	
	private Timestamp firstUsed;
	
	private Timestamp lastUsed;

}
