package com.cooksys.social_media.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {
	
	User dtoToEntity(UserRequestDto userRequestDto);
	
	// We add the mapping value here because the User type doesn't have a "username" field in it.
	// The username field is stored in the nested credentials object.
	@Mapping(target = "username", source = "credentials.username")
	UserResponseDto entityToDto(User user);

	List<UserResponseDto> enitiesToDtos(List<User> users);
	

}
