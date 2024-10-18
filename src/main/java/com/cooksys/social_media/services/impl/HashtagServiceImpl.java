package com.cooksys.social_media.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.HashtagMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.repositories.HashtagRepository;
import com.cooksys.social_media.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	
	private final HashtagMapper hashtagMapper;
	private final TweetMapper tweetMapper;
	
	private Hashtag getHashtag(String label) {
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		if (optionalHashtag.isEmpty()) {
			throw new NotFoundException("No hashtag with the provided label!");
		}
		return optionalHashtag.get();
	}
	
	@Override
	public List<HashtagDto> getAllTags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	@Override
	public List<TweetResponseDto> getTweetsByHashtagLabel(String label) {
		return tweetMapper.entitiesToDtos(getHashtag(label).getTweets());
	}

}
