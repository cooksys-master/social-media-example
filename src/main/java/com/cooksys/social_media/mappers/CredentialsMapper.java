package com.cooksys.social_media.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	
	Credentials dtoToEmbeddable(CredentialsDto credentialsRequestDto);

}
