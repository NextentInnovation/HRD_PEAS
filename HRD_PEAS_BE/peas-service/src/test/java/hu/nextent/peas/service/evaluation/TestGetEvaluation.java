package hu.nextent.peas.service.evaluation;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
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
import hu.nextent.peas.exceptions.BaseResponseException;
import hu.nextent.peas.exceptions.ExceptionLabelConstant;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.model.EvaluationModel;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestGetEvaluation extends TestEvaluationBase {

	
	@Test
	@Rollback
	public void testEvaluation() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstEvaluationByStatus(getSelectedUser(), EvaluationStatusEnum.EVALUATING);
		mockAuthService();
		
		ResponseEntity<EvaluationModel> respEval = evaluationService.getEvaluation(getSelectedEvaluation().getId());
		
		Assert.assertNotNull(respEval);
		Assert.assertEquals(respEval.getStatusCode(), HttpStatus.OK);
		Assert.assertNotNull(respEval.getBody());
		Assert.assertEquals(getSelectedEvaluation().getId(), respEval.getBody().getId());
		Assert.assertNotNull(respEval.getBody().getFactors());
		Assert.assertTrue(!respEval.getBody().getFactors().isEmpty());
	}
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	@Rollback
	public void testNotFoundedEvaluation() {
		givenUser(TestConstant.USER_NORMAL);
		givenFirstEvaluationByStatus(getSelectedUser(), EvaluationStatusEnum.EVALUATING);
		mockAuthService();
		
		thrown.expect(BaseResponseException.class);
		@SuppressWarnings("unused")
		ResponseEntity<EvaluationModel> respEval = evaluationService.getEvaluation(-1L);
		
		thrown.expectMessage(ExceptionLabelConstant.ERROR_EVALUATION_NOT_FOUNDED);
	}
}
