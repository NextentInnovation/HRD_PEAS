package hu.nextent.peas.facades;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.service.TestServiceConfig;
import lombok.val;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestToDoEvaluationCloserFacade {

	@Autowired private UserRepository userRepository;
	@Autowired private EvaluationRepository evaluationRepository;
	
	@Autowired private ToDoEvaluationCloserFacade tested;
	
	private User user;
	
	private void givenUser(String username) {
		val userOpt = userRepository.findByUserNameAndCompany_Name(username, TestConstant.DEFAULT_COMPANY);
		user = userOpt.get();
	}
	
	@Test
	public void testCloseToDo() {
		givenUser(TestConstant.USER_NORMAL);
		// Keresek egy értékelhető taskot
		val evaluations = evaluationRepository.findAllByEvaluatorAndStatusOrderById(user, EvaluationStatusEnum.EVALUATING);
		Assert.assertTrue(!evaluations.isEmpty());
		
		Evaluation eval = evaluations.get(0);
		eval.setStatus(EvaluationStatusEnum.EVALUATED);
		evaluationRepository.save(eval);
		
		// WHEN
		tested.apply(eval);
		
		// ASSERT
		
		
	}
	
}
