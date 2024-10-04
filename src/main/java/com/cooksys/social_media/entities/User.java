package com.cooksys.social_media.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp joined;
	
	@Column(nullable = false)
	private Boolean deleted;
	
	@Embedded
	private Credentials credentials;
	
	@Embedded
	private Profile profile;
	
	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "user_likes")
	private List<Tweet> likes = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "user_mentions")
	private List<Tweet> mentions = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "followers_following")
	private List<User> followers = new ArrayList<>();
	
	@ManyToMany(mappedBy = "followers")
	private List<User> following = new ArrayList<>();

}
