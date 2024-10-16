package com.cooksys.social_media.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {
	
	CredentialsDto credentials;
	
	ProfileDto profile;

}
