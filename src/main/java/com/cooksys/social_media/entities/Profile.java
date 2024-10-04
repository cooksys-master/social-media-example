package com.cooksys.social_media.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Profile {
	
	private String firstName;
	
	private String lastName;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	private String phone;

}
