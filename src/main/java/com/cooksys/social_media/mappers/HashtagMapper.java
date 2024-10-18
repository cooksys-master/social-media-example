package com.cooksys.social_media.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
	
	HashtagDto entityToDto(Hashtag hashtag);
	
	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);

}
