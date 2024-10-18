package com.cooksys.social_media.services;

import java.util.List;

import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;

public interface UserService {
	
	User getUser(String username);
	
	void validateUser(User user, CredentialsDto credentialsDto);
	
	void validateCredentialsDto(CredentialsDto credentialsDto);

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUsername(String username);

	UserResponseDto createUser(UserRequestDto userRequestDto);

	UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto);

	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

	void followUser(String username, CredentialsDto credentialsDto);

	void unfollowUser(String username, CredentialsDto credentialsDto);

	List<UserResponseDto> getFollowers(String username);

	List<UserResponseDto> getFollowing(String username);

	List<TweetResponseDto> getUserFeed(String username);

	List<TweetResponseDto> getUserTweets(String username);

	List<TweetResponseDto> getUserMentions(String username);

}
