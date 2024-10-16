package com.cooksys.social_media.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.UserMapper;
import com.cooksys.social_media.repositories.UserRepository;
import com.cooksys.social_media.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.enitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		if (userRequestDto == null || userRequestDto.getProfile() == null || userRequestDto.getCredentials() == null
				|| userRequestDto.getProfile().getEmail() == null
				|| userRequestDto.getCredentials().getUsername() == null
				|| userRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException("Must provide the minimum required fields!");
		}
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername().toLowerCase());
		if (optionalUser.isPresent()) {
			User userInDB = optionalUser.get();
			if (userInDB.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
				userInDB.setDeleted(false);
				userRepository.save(userInDB);
				return userMapper.entityToDto(userInDB);
			}
			throw new BadRequestException("Username already taken");
		}
		User userToCreate = userMapper.dtoToEntity(userRequestDto);
		userToCreate.setDeleted(false);
		userToCreate.getCredentials().setUsername(userToCreate.getCredentials().getUsername().toLowerCase());
		return userMapper.entityToDto(userRepository.save(userToCreate));
	}

	@Override
	public UserResponseDto getUserByUsername(String parameter) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(parameter.toLowerCase());
		if (optionalUser.isPresent()) {
			return userMapper.entityToDto(optionalUser.get());
		}
		throw new NotFoundException();
	}

//	@Override
//	public List<UserResponseDto> getAllUsers() {
//		return userMapper.enitiesToDtos(userRepository.findAll().stream().filter(user -> !user.getDeleted()).toList());
//	}

//	@Override
//	public List<UserResponseDto> getAllUsers() {
//		List<User> users = userRepository.findAll();
//		List<UserResponseDto> result = new ArrayList<>();
//		for (User u : users) {
//			if (!u.getDeleted()) {
//				result.add(userMapper.entityToDto(u));
//			}
//		}
//		return result;
//	}

}
