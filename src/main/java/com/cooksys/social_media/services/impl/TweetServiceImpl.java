package com.cooksys.social_media.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.dtos.ContextDto;
import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.HashtagMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.mappers.UserMapper;
import com.cooksys.social_media.repositories.HashtagRepository;
import com.cooksys.social_media.repositories.TweetRepository;
import com.cooksys.social_media.repositories.UserRepository;
import com.cooksys.social_media.services.TweetService;
import com.cooksys.social_media.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;
	
	private final UserService userService;
	
//	private List<String> extractUsernamesOrHashtags(String content, String delimiter) {
//		String[] splitString = content.split(delimiter);
//		ArrayList<String> result = new ArrayList<>();
//		for (int i = 1; i < splitString.length; i++) {
//			String value = splitString[i].split(" ")[0];
//			result.add(value);
//		}
//		return result;
//	}
	
	private List<String> extractUsernamesOrHashtags(String content, String delimiter) {
		List<String> result = new ArrayList<>();
		
		String regex = "\\" + delimiter + "([^\\s]+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		
		while(matcher.find()) {
			if(matcher.groupCount() > 0) {
				result.add(matcher.group(1));
			}
		}
		return result;
	}
	
	private List<User> parseMentions(String content) {
		List<User> mentions = new ArrayList<>();
		for (String username : extractUsernamesOrHashtags(content, "@")) {
			Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
			optionalUser.ifPresent(mentions::add);
		}
		return mentions;
	}
	
	private List<Hashtag> parseHashtags(String content) {
		List<Hashtag> hashtags = new ArrayList<>();
		for (String label : extractUsernamesOrHashtags(content, "#")) {
			Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
			if (optionalHashtag.isPresent()) {
				Hashtag hashtagToUpdate = optionalHashtag.get();
				hashtagToUpdate.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
				hashtagRepository.save(hashtagToUpdate);
				hashtags.add(hashtagToUpdate);
			} else {
				Hashtag hashtagToCreate = new Hashtag();
				hashtagToCreate.setLabel(label);
				hashtagToCreate.setLastUsed(Timestamp.valueOf(LocalDateTime.now()));
				hashtagRepository.save(hashtagToCreate);
				hashtags.add(hashtagToCreate);
			}
		}
		return hashtags;
		
	}
	
	private Tweet getTweet(Integer id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if (optionalTweet.isEmpty()) {
			throw new NotFoundException("No Tweet with the provided id");
		}
		return optionalTweet.get();
	}
	
	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalseOrderByPostedDesc());
	}
	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		userService.validateCredentialsDto(tweetRequestDto.getCredentials());
		User author = userService.getUser(tweetRequestDto.getCredentials().getUsername());
		userService.validateUser(author, tweetRequestDto.getCredentials());
		
		if (tweetRequestDto.getContent() == null) {
			throw new BadRequestException("You must provide content for a Tweet!");
		}
		
		Tweet tweetToCreate = new Tweet();
		tweetToCreate.setContent(tweetRequestDto.getContent());
		tweetToCreate.setAuthor(author);
		tweetToCreate.setDeleted(false);
		tweetToCreate.setMentions(parseMentions(tweetToCreate.getContent()));
		tweetToCreate.setHashtags(parseHashtags(tweetToCreate.getContent()));
		
		return tweetMapper.entityToDto(tweetRepository.save(tweetToCreate));
	}
	@Override
	public TweetResponseDto getTweetById(Integer id) {
		return tweetMapper.entityToDto(getTweet(id));
	}
	@Override
	public TweetResponseDto deleteTweet(Integer id, CredentialsDto credentialsDto) {
		userService.validateCredentialsDto(credentialsDto);
		User author = userService.getUser(credentialsDto.getUsername());
		userService.validateUser(author, credentialsDto);
		Tweet tweetToDelete = getTweet(id);
		if (!tweetToDelete.getAuthor().equals(author)) {
			throw new BadRequestException("You can only delete your own Tweets!");
		}
		tweetToDelete.setDeleted(true);
		return tweetMapper.entityToDto(tweetRepository.save(tweetToDelete));
	}
	@Override
	public void likeTweet(Integer id, CredentialsDto credentialsDto) {
		userService.validateCredentialsDto(credentialsDto);
		User userWhoLikes = userService.getUser(credentialsDto.getUsername());
		userService.validateUser(userWhoLikes, credentialsDto);
		Tweet tweetToLike = getTweet(id);
		if (!tweetToLike.getLikes().contains(userWhoLikes)) {
			tweetToLike.getLikes().add(userWhoLikes);
			tweetRepository.save(tweetToLike);
		}
	}
	@Override
	public TweetResponseDto createReply(Integer id, TweetRequestDto tweetRequestDto) {
		userService.validateCredentialsDto(tweetRequestDto.getCredentials());
		User author = userService.getUser(tweetRequestDto.getCredentials().getUsername());
		userService.validateUser(author, tweetRequestDto.getCredentials());
		Tweet inReplyTo = getTweet(id);
		
		if (tweetRequestDto.getContent() == null) {
			throw new BadRequestException("You must provide content for a Tweet!");
		}
		
		Tweet tweetToCreate = new Tweet();
		tweetToCreate.setContent(tweetRequestDto.getContent());
		tweetToCreate.setAuthor(author);
		tweetToCreate.setDeleted(false);
		tweetToCreate.setInReplyTo(inReplyTo);
		tweetToCreate.setMentions(parseMentions(tweetToCreate.getContent()));
		tweetToCreate.setHashtags(parseHashtags(tweetToCreate.getContent()));
		
		return tweetMapper.entityToDto(tweetRepository.save(tweetToCreate));
	}
	
	@Override
	public TweetResponseDto createRepost(Integer id, CredentialsDto credentialsDto) {
		userService.validateCredentialsDto(credentialsDto);
		User author = userService.getUser(credentialsDto.getUsername());
		userService.validateUser(author, credentialsDto);
		Tweet repostOf = getTweet(id);
		
		
		Tweet tweetToCreate = new Tweet();
		tweetToCreate.setAuthor(author);
		tweetToCreate.setDeleted(false);
		tweetToCreate.setRepostOf(repostOf);
		
		return tweetMapper.entityToDto(tweetRepository.save(tweetToCreate));
	}
	
	@Override
	public List<HashtagDto> getTweetTags(Integer id) {
		return hashtagMapper.entitiesToDtos(getTweet(id).getHashtags());
	}
	
	@Override
	public List<UserResponseDto> getTweetLikes(Integer id) {
		return userMapper.enitiesToDtos(getTweet(id).getLikes());
	}
	
	@Override
	public ContextDto getTweetContext(Integer id) {
		Tweet target = getTweet(id);
		ContextDto context = new ContextDto();
		context.setTarget(tweetMapper.entityToDto(target));
		
		List<Tweet> before = new ArrayList<>();
		for (Tweet inReplyTo = target.getInReplyTo(); inReplyTo != null; inReplyTo = inReplyTo.getInReplyTo()) {
			before.add(inReplyTo);
		}
		context.setBefore(tweetMapper.entitiesToDtos(before));
		
		List<Tweet> after = new ArrayList<>(target.getReplies());
		for (Tweet reply : after) {
			after.addAll(reply.getReplies());
		}
		context.setAfter(tweetMapper.entitiesToDtos(after));
		
		return context;
	}
	@Override
	public List<TweetResponseDto> getTweetReplies(Integer id) {
		return tweetMapper.entitiesToDtos(getTweet(id).getReplies().stream().filter(reply -> !reply.getDeleted()).toList());
	}
	@Override
	public List<TweetResponseDto> getTweetReposts(Integer id) {
		return tweetMapper.entitiesToDtos(getTweet(id).getReposts());
	}
	@Override
	public List<UserResponseDto> getTweetMentions(Integer id) {
		return userMapper.enitiesToDtos(getTweet(id).getMentions());
	}

}
