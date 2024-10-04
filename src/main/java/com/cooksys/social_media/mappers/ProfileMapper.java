package com.cooksys.social_media.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_media.dtos.ProfileRequestDto;
import com.cooksys.social_media.dtos.ProfileResponseDto;
import com.cooksys.social_media.entities.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	
	Profile dtoToEmbeddable(ProfileRequestDto profileRequestDto);
	
	ProfileResponseDto embeddableToDto(Profile profile);

}
