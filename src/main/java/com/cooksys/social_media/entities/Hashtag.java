package com.cooksys.social_media.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Hashtag {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String label;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp firstUsed;
	
	@UpdateTimestamp
	private Timestamp lastUsed;
	
	@ManyToMany(mappedBy = "hashtags")
	@OrderBy("posted DESC")
	@SQLRestriction("deleted = false")
	private List<Tweet> tweets = new ArrayList<>();

}
