package com.cooksys.social_media.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.entities.User;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
	
	List<Tweet> findAllByDeletedFalseOrderByPostedDesc();
	
	List<Tweet> findAllByAuthorAndDeletedFalseOrderByPostedDesc(User author);

	Optional<Tweet> findByIdAndDeletedFalse(Integer id);

}
