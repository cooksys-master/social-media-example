package com.cooksys.social_media.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.social_media.dtos.TweetDto;
import com.cooksys.social_media.entities.Tweet;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {
	
	TweetDto entityToDto(Tweet tweet);
	
	List<TweetDto> entitiesToDtos(List<Tweet> tweets);

}
