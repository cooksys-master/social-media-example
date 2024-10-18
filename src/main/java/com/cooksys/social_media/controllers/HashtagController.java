package com.cooksys.social_media.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashtagController {
	
	private final HashtagService hashtagService;
	
	@GetMapping
	public List<HashtagDto> getAllTags() {
		return hashtagService.getAllTags();
	}
	
	@GetMapping("/{label}")
	public List<TweetResponseDto> getTweetsByHashtagLabel(@PathVariable String label) {
		return hashtagService.getTweetsByHashtagLabel(label);
	}

}
