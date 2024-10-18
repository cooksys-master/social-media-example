package com.cooksys.social_media.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_table")
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
	@OrderBy("posted DESC")
	@SQLRestriction("deleted = false")
	private List<Tweet> tweets = new ArrayList<>();

	@ManyToMany(mappedBy = "likes")
	@OrderBy("posted DESC")
	@SQLRestriction("deleted = false")
	private List<Tweet> likes = new ArrayList<>();

	@ManyToMany(mappedBy = "mentions")
	@OrderBy("posted DESC")
	@SQLRestriction("deleted = false")
	private List<Tweet> mentions = new ArrayList<>();

	@ManyToMany
	@SQLRestriction("deleted = false")
	@JoinTable(name = "followers_following", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "followed_id"))
	private List<User> followers = new ArrayList<>();

	@ManyToMany(mappedBy = "followers")
	@SQLRestriction("deleted = false")
	private List<User> following = new ArrayList<>();

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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

}
