package hu.nextent.peas.service;

import org.springframework.http.ResponseEntity;

import hu.nextent.peas.model.RatingSendModel;

public interface RatingService {

	ResponseEntity<Void> saveRating(RatingSendModel body, Long ratingId);
	
}
