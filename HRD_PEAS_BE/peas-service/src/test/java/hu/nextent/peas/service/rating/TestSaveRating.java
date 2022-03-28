package hu.nextent.peas.service.rating;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.entity.Notification;
import hu.nextent.peas.jpa.entity.NotificationTypeEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.entity.ToDoStatusEnum;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.RatingSendModel;
import hu.nextent.peas.service.RatingService;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestSaveRating extends TestServiceBase {

	private static final String TEXT = "Ez egy értékelés";

	private static final String TEXT_GRADE = "Sorsolásra sorolom juttatás be javaslat változás";

	@Autowired
	RatingRepository ratingRepository;
	
	@Autowired
	RatingService testedService;
	
	@Autowired
	PeriodRepository periodRepository;
	
	@Autowired
	ToDoRepository toDoRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	User worker;
	Period period;
	Rating rating;
	RatingSendModel ratingSendModel;
	
	@Test
	@Rollback
	public void testSaveRating() {
		givenUser(TestConstant.USER_LEADER);
		mockAuthService();
		givenFirstWorker();
		givenPeriodRating();
		givenRating();
		givenRatingSendModel();

		// WHEN
		ResponseEntity<Void> resp = testedService.saveRating(ratingSendModel, rating.getId());
		
		// ASSERT
		Assert.assertNotNull(resp);
		Assert.assertEquals(resp.getStatusCode(), HttpStatus.OK);
		
		rating = ratingRepository.getOne(rating.getId());
		Assert.assertEquals(TEXT, rating.getTextualEvaluation());
		Assert.assertEquals(TEXT_GRADE, rating.getGradeRecommendation());
		
		
		// Vezetői Todo lekérdezése
		List<ToDo> todos = toDoRepository.findAllByRatingAndToDoType(rating, ToDoTypeEnum.RATING);
		for(ToDo todo: todos) {
			Assert.assertEquals(ToDoStatusEnum.CLOSE, todo.getStatus());
		}
		
		// Notifikáció létrejött vezetői
		List<Notification> notifs = notificationRepository.findAllByToUserAndRatingAndNotificationType(getSelectedUser(), rating, NotificationTypeEnum.RATING_END);
		Assert.assertEquals(1, notifs.size());
		
		// Notifikáció létrejött dolgozói
		notifs = notificationRepository.findAllByToUserAndRatingAndNotificationType(worker, rating, NotificationTypeEnum.RATING_END);
		Assert.assertEquals(1, notifs.size());
		
		
	}
	
	private void givenFirstWorker() {
		List<User> childUser = userRepository.findByLeader(getSelectedUser());
		worker = childUser.get(0);
	}

	private void givenRatingSendModel() {
		ratingSendModel = new RatingSendModel();
		ratingSendModel.setCooperation(true);
		ratingSendModel.setGradeRecommendation(TEXT_GRADE);
		ratingSendModel.setTextualEvaluation(TEXT);
	}
	
	private void givenPeriodRating() {
		period = periodRepository.findByCompanyAndStatus(getSelectedUser().getCompany(), PeriodStatusEnum.RATING).get();
	}
	
	private void givenRating() {
		rating = ratingRepository.findByPeriodAndUser(period, worker).get();
	}
}
