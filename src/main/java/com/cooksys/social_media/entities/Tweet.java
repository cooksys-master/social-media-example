package com.cooksys.social_media.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Tweet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp posted;
	
	@Column(nullable = false)
	private Boolean deleted;
	
	private String content;
	
	@ManyToOne
	@JoinColumn
	private User author;
	
	@ManyToOne
	@JoinColumn
	private Tweet inReplyTo;
	
	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn
	private Tweet repostOf;
	
	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> resposts = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "tweet_hashtags")
	private List<Hashtag> hashtags = new ArrayList<>();
	
	@ManyToMany(mappedBy = "likes")
	private List<User> likes = new ArrayList<>();
	
	@ManyToMany(mappedBy = "mentions")
	private List<User> mentions = new ArrayList<>();

}
