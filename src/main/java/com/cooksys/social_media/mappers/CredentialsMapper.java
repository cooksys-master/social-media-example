package com.cooksys.social_media.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_media.dtos.CredentialsRequestDto;
import com.cooksys.social_media.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	
	Credentials dtoToEmbeddable(CredentialsRequestDto credentialsRequestDto);

}
