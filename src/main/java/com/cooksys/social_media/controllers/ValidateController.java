package com.cooksys.social_media.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media.services.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {
	
	private final ValidateService validateService;

	@GetMapping("/tag/exists/{label}")
	public Boolean validateHashtagExists(@PathVariable String label) {
		return validateService.validateHastagExists(label);
	}
	
	@GetMapping("/username/exists/@{username}")
	public boolean validateUsernameExists(@PathVariable String username) {
		return validateService.validateUsernameExists(username);
	}
	
	@GetMapping("/username/available/@{username}")
	public boolean validateUsernameAvailable(@PathVariable String username) {
		return validateService.validateUsernameAvailable(username);
	}

}
