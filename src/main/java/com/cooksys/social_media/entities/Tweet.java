package com.cooksys.social_media.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

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
public class Tweet implements Comparable<Tweet> {

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
	@SQLRestriction("deleted = false")
	private User author;

	@ManyToOne
	@JoinColumn
	private Tweet inReplyTo;

	@OneToMany(mappedBy = "inReplyTo")
//	@OrderBy("posted DESC")
//	@SQLRestriction("deleted = false")
	private List<Tweet> replies = new ArrayList<>();

	@ManyToOne
	@JoinColumn
	private Tweet repostOf;

	@OneToMany(mappedBy = "repostOf")
//	@OrderBy("posted DESC")
	@SQLRestriction("deleted = false")
	private List<Tweet> reposts = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "tweet_hashtags")
	private List<Hashtag> hashtags = new ArrayList<>();

	@ManyToMany
	@SQLRestriction("deleted = false")
	@JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> likes = new ArrayList<>();

	@ManyToMany
	@SQLRestriction("deleted = false")
	@JoinTable(name = "user_mentions", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> mentions = new ArrayList<>();

	@Override
	public int compareTo(Tweet that) {
		if (this == that) {
			return 0;
		}
		return this.getPosted().compareTo(that.getPosted());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		return Objects.equals(id, other.id);
	}

}
