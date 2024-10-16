package com.cooksys.social_media.services;

public interface ValidateService {

	Boolean validateHastagExists(String label);

	boolean validateUsernameExists(String username);

	boolean validateUsernameAvailable(String username);

}
