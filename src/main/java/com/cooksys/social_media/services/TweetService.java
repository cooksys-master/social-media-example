package com.cooksys.social_media.services;

import java.util.List;

import com.cooksys.social_media.dtos.ContextDto;
import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto getTweetById(Integer id);

	TweetResponseDto deleteTweet(Integer id, CredentialsDto credentialsDto);

	void likeTweet(Integer id, CredentialsDto credentialsDto);

	TweetResponseDto createReply(Integer id, TweetRequestDto tweetRequestDto);

	TweetResponseDto createRepost(Integer id, CredentialsDto credentialsDto);

	List<HashtagDto> getTweetTags(Integer id);

	List<UserResponseDto> getTweetLikes(Integer id);

	ContextDto getTweetContext(Integer id);

	List<TweetResponseDto> getTweetReplies(Integer id);

	List<TweetResponseDto> getTweetReposts(Integer id);

	List<UserResponseDto> getTweetMentions(Integer id);

}
