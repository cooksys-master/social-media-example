package com.cooksys.social_media.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.repositories.HashtagRepository;
import com.cooksys.social_media.repositories.UserRepository;
import com.cooksys.social_media.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;

	@Override
	public Boolean validateHastagExists(String label) {
		return hashtagRepository.findByLabel(label.toLowerCase()).isPresent();
	}

	@Override
	public boolean validateUsernameExists(String username) {
		return userRepository.findByCredentialsUsername(username.toLowerCase()).isPresent();
	}

	@Override
	public boolean validateUsernameAvailable(String username) {
		return userRepository.findByCredentialsUsername(username.toLowerCase()).isEmpty();
	}

}
