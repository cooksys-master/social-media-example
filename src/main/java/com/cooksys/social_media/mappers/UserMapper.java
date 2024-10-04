package com.cooksys.social_media.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {
	
	User dtoToEntity(UserRequestDto userRequestDto);
	
	@Mapping(target = "username", source = "credentials.username")
	UserResponseDto entityToDto(User user);
	

}
