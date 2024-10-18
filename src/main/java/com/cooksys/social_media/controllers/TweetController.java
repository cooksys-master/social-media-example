package com.cooksys.social_media.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.dtos.ContextDto;
import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetRequestDto;
import com.cooksys.social_media.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {
	
	private final TweetService tweetService;
	
	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
		return tweetService.getAllTweets();
	}
	
	@PostMapping
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createTweet(tweetRequestDto);
	}
	
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Integer id) {
		return tweetService.getTweetById(id);
	}
	
	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@PathVariable Integer id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.deleteTweet(id, credentialsDto);
	}
	
	@PostMapping("/{id}/like")
	public void likeTweet(@PathVariable Integer id, @RequestBody CredentialsDto credentialsDto) {
		tweetService.likeTweet(id, credentialsDto);
	}
	
	@PostMapping("/{id}/reply")
	public TweetResponseDto createReply(@PathVariable Integer id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.createReply(id, tweetRequestDto);
	}
	
	@PostMapping("/{id}/repost")
	public TweetResponseDto createRepost(@PathVariable Integer id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.createRepost(id, credentialsDto);
	}
	
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getTweetTags(@PathVariable Integer id) {
		return tweetService.getTweetTags(id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getTweetLikes(@PathVariable Integer id) {
		return tweetService.getTweetLikes(id);
	}
	
	@GetMapping("/{id}/context")
	public ContextDto getTweetContext(@PathVariable Integer id) {
		return tweetService.getTweetContext(id);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getTweetReplies(@PathVariable Integer id) {
		return tweetService.getTweetReplies(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetReposts(@PathVariable Integer id) {
		return tweetService.getTweetReposts(id);
	}
	
	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getTweetMentions(@PathVariable Integer id) {
		return tweetService.getTweetMentions(id);
	}

}
