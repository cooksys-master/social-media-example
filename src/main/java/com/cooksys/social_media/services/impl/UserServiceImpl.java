package com.cooksys.social_media.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.dtos.TweetDto;
import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotAuthorizedException;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.mappers.UserMapper;
import com.cooksys.social_media.repositories.UserRepository;
import com.cooksys.social_media.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final TweetMapper tweetMapper;

	private User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username.toLowerCase());
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user with the provided username!");
		}
		return optionalUser.get();
	}

	private void validateUser(User user, CredentialsDto credentialsDto) {
		if (!user.getCredentials().getUsername().equals(credentialsDto.getUsername().toLowerCase())
				|| !user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new NotAuthorizedException("The username & password combination does not match the stored user.");
		}
	}

	private void validateCredentialsDto(CredentialsDto credentialsDto) {
		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new BadRequestException("Must provide credentials!");
		}
	}
	
	private List<Tweet> filterAndSortTweets(List<Tweet> tweets) {
		return tweets
				.stream()
				.filter(tweet -> !tweet.getDeleted())
				.sorted((tweet1, tweet2) -> tweet1.getPosted().compareTo(tweet2.getPosted()))
				.toList();
	}

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
		Optional<User> optionalUser = userRepository
				.findByCredentialsUsername(userRequestDto.getCredentials().getUsername().toLowerCase());
		if (optionalUser.isPresent()) {
			User userInDB = optionalUser.get();
			if (userInDB.getDeleted()
					&& userInDB.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
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
		return userMapper.entityToDto(getUser(parameter));
	}

	@Override
	public UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto) {
		if (userRequestDto == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Must provide some data to update!");
		}
		validateCredentialsDto(userRequestDto.getCredentials());
		User userToUpdate = getUser(username);
		validateUser(userToUpdate, userRequestDto.getCredentials());
		if (userRequestDto.getProfile().getFirstName() != null) {
			userToUpdate.getProfile().setFirstName(userRequestDto.getProfile().getFirstName());
		}
		if (userRequestDto.getProfile().getLastName() != null) {
			userToUpdate.getProfile().setLastName(userRequestDto.getProfile().getLastName());
		}
		if (userRequestDto.getProfile().getEmail() != null) {
			userToUpdate.getProfile().setEmail(userRequestDto.getProfile().getEmail());
		}
		if (userRequestDto.getProfile().getPhone() != null) {
			userToUpdate.getProfile().setPhone(userRequestDto.getProfile().getPhone());
		}
		return userMapper.entityToDto(userRepository.save(userToUpdate));
	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		validateCredentialsDto(credentialsDto);
		User userToDelete = getUser(username);
		validateUser(userToDelete, credentialsDto);
		userToDelete.setDeleted(true);
		return userMapper.entityToDto(userRepository.save(userToDelete));
	}

	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		validateCredentialsDto(credentialsDto);
		User userToFollow = getUser(username);
		User userToSubcribe = getUser(credentialsDto.getUsername());
		validateUser(userToSubcribe, credentialsDto);
		if (userToFollow.getFollowers().contains(userToSubcribe)) {
			throw new BadRequestException("Already a follower relationship!");
		}
		userToFollow.getFollowers().add(userToSubcribe);
		userRepository.save(userToFollow);
	}

	@Override
	public void unfollowUser(String username, CredentialsDto credentialsDto) {
		validateCredentialsDto(credentialsDto);
		User userToUnfollow = getUser(username);
		User userToUnsubcribe = getUser(credentialsDto.getUsername());
		validateUser(userToUnsubcribe, credentialsDto);
		if (!userToUnfollow.getFollowers().contains(userToUnsubcribe)) {
			throw new BadRequestException("No follower relationsip exists between the two provided users.");
		}
		userToUnfollow.getFollowers().remove(userToUnsubcribe);
		userRepository.save(userToUnfollow);
	}

	@Override
	public List<UserResponseDto> getFollowers(String username) {
		User userToGetFollowers = getUser(username);
		List<UserResponseDto> result = new ArrayList<>();
		for (User follower : userToGetFollowers.getFollowers()) {
			if (!follower.getDeleted()) {
				result.add(userMapper.entityToDto(follower));
			}
		}
		return result;
	}

	@Override
	public List<UserResponseDto> getFollowing(String username) {
		return userMapper
				.enitiesToDtos(
						getUser(username)
						.getFollowing()
						.stream()
						.filter(user -> !user.getDeleted())
						.toList()
				);
	}

	@Override
	public List<TweetDto> getUserFeed(String username) {
		User userToGetFeed = getUser(username);
		List<Tweet> userFeed = userToGetFeed.getTweets();
		for (User following : userToGetFeed.getFollowing()) {
			userFeed.addAll(following.getTweets());
		}
		return tweetMapper.entitiesToDtos(filterAndSortTweets(userFeed));
	}

	@Override
	public List<TweetDto> getUserTweets(String username) {
		User userToGetTweets = getUser(username);
		return tweetMapper.entitiesToDtos(filterAndSortTweets(userToGetTweets.getTweets()));
	}

	@Override
	public List<TweetDto> getUserMentions(String username) {
		User userToGetMentions = getUser(username);
		return tweetMapper.entitiesToDtos(filterAndSortTweets(userToGetMentions.getMentions()));
	}

}
