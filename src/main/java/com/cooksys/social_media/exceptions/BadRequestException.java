package com.cooksys.social_media.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6185866314805578675L;
	
	private String message;

}
