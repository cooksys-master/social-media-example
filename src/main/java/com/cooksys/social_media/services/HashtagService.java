package com.cooksys.social_media.services;

import java.util.List;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getAllTags();

	List<TweetResponseDto> getTweetsByHashtagLabel(String label);

}
