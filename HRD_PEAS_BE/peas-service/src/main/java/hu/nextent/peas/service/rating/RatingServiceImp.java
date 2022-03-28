package hu.nextent.peas.service.rating;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.facades.AutomaticTaskRatingAddRatingFacade;
import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.facades.ToDoEvaluationCloserFacade;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import hu.nextent.peas.model.RatingSendModel;
import hu.nextent.peas.service.RatingService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class RatingServiceImp extends AbstractRatingServiceImp implements RatingService {

	@Autowired
	private AutomaticTaskRatingAddRatingFacade automaticTaskRatingAddRatingFacade;
	
	@Autowired 
	private ToDoEvaluationCloserFacade toDoEvaluationCloserFacede;
	
	@Autowired
	private FactoryServiceNotification factoryServiceNotification;
	
	@Override
	public ResponseEntity<Void> saveRating(RatingSendModel body, Long ratingId) {
		log.debug("saveRating, ratingId: {}, body: {}", ratingId, body);
		validate(body, ratingId); // Validáljuk az adatokat
		Rating rating = ratingRepository.getOne(ratingId);
		rating = save(body, rating); // Menjük az adatokat
		automaticTaskRatingAddRatingFacade.apply(rating); // Automatikus task sikerrese értékelés hozzáadása
		toDoEvaluationCloserFacede.apply(rating); // Vezetői ToDo zárása
		// Notifikáció küldés, hogy lezárták a vezetői értékelést
		factoryServiceNotification.createRatingEnd(rating);
		ratingRepository.flush();
		log.debug("saveRating, end, ratingId: {}, gradeRecommendation: {}, textualEvaluation: {}", ratingId, rating.getGradeRecommendation(), rating.getTextualEvaluation());
		return ResponseEntity.ok().build();
	}

	private void validate(RatingSendModel body, Long checkedRatingId) {
		checkRight();
		if (checkedRatingId == null) {
			throw ExceptionList.rating_id_reqired();
    	}
		Rating rating = checkExists(checkedRatingId);
		checkCompany(rating);
		checkStatus(rating, RatingStatusEnum.OPEN);
		checkMyEmployee(rating);
		
		if (StringUtils.isEmpty(body.getGradeRecommendation())) {
			throw ExceptionList.rating_gradeRecommendation_empty(checkedRatingId);
		}

		if (StringUtils.isEmpty(body.getTextualEvaluation())) {
			throw ExceptionList.rating_textualEvaluation_empty(checkedRatingId);
		}

		if (body.isCooperation() == null) {
			throw ExceptionList.rating_cooperation_empty(checkedRatingId);
		}
		
	}

	private Rating save(RatingSendModel body, Rating rating) {
		log.debug("body text:\ngradeRecommendation: {}\ntextualEvaluation:{}", body.getGradeRecommendation(), body.getTextualEvaluation());
		
		rating.setCooperation(body.isCooperation());
		rating.setGradeRecommendation(body.getGradeRecommendation());
		rating.setTextualEvaluation(body.getTextualEvaluation());
		rating.setStatus(RatingStatusEnum.EVALUATED);
		rating.setRatingStartDate(OffsetDateTime.now(ZoneOffset.UTC));
		ratingRepository.save(rating);
		return rating;
	}


	
	
	
}
