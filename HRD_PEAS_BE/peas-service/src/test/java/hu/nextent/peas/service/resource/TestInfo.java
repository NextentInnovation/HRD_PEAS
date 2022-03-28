package hu.nextent.peas.service.resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import hu.nextent.peas.jpa.dao.CompanyVirtueRepository;
import hu.nextent.peas.jpa.dao.DifficultyRepository;
import hu.nextent.peas.jpa.dao.FactorRepository;
import hu.nextent.peas.jpa.dao.LeaderVirtueRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.PeriodStatusEnum;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.model.PeasAppInfoModel;
import hu.nextent.peas.service.ResourceService;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestInfo extends TestServiceBase {

	// Vizsgált elem
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private DifficultyRepository difficultyRepository;
	
	@Autowired
	private CompanyVirtueRepository companyVirtueRepository;
	
	@Autowired
	private LeaderVirtueRepository leaderVirtueRepository;
	
	@Autowired
	private PeriodRepository periodRepository;
	
	@Autowired
	private FactorRepository factorRepository;
	
	@Test
	@Rollback
	public void testNormalUserInfo() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		ResponseEntity<PeasAppInfoModel> respInfoModel = resourceService.getInfo();
		
		Assert.assertNotNull(respInfoModel);
		Assert.assertEquals(respInfoModel.getStatusCode(), HttpStatus.OK);
		PeasAppInfoModel info = respInfoModel.getBody();
		Assert.assertNotNull(info.getParameters());
		Assert.assertNotNull(info.getLeader());
		Assert.assertNotNull(info.getDifficulties());
		Assert.assertNotNull(info.getTaskstatuses());
		Assert.assertNotNull(info.getTasktypes());
		Assert.assertNotNull(info.getEvaluationstatuses());
		Assert.assertNotNull(info.getCompanyVirtues());
		Assert.assertNotNull(info.getLeaderVirtues());
		Assert.assertNotNull(info.getActivePeriod());
		Assert.assertNotNull(info.getPeriodstatuses());
		Assert.assertNotNull(info.getFactors());
		
		// Parameters
		Assert.assertTrue(info.getParameters() instanceof Map);
		@SuppressWarnings("unchecked")
		Map<String, String> parameters = (Map<String, String>)info.getParameters();
		Assert.assertTrue(!parameters.isEmpty());
		Assert.assertTrue(parameters.containsKey("page.size"));
		// UserModel
		Assert.assertEquals(getSelectedUser().getId(), info.getCurrentUser().getId());
		// Leader
		Assert.assertEquals(getSelectedUser().getLeader().getId(), info.getLeader().getId());
		// Difficulty
		List<?> difficulties = difficultyRepository.findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(getSelectedUser().getCompany());
		Assert.assertEquals(difficulties.size(), info.getDifficulties().size());
		// taskstatuses
		Assert.assertEquals(TaskStatusEnum.values().length, info.getTaskstatuses().size());
		// tasktypes
		Assert.assertEquals(TaskTypeEnum.values().length, info.getTasktypes().size());
		// evaluationstatuses
		Assert.assertEquals(EvaluationStatusEnum.values().length, info.getEvaluationstatuses().size());
		// companyVirtues
		List<?> compVirts = companyVirtueRepository.findAllByActiveTrueAndCompanyAndValueIsNotNullOrderByValue(getSelectedUser().getCompany());
		Assert.assertEquals(compVirts.size(), info.getCompanyVirtues().size());
		// leaderVirtues
		List<?> leaderVirts = leaderVirtueRepository.findAllByActiveTrueAndOwnerOrderByValue(getSelectedUser().getLeader());
		Assert.assertEquals(leaderVirts.size(), info.getLeaderVirtues().size());
		// activePeriod
		Optional<Period> optActivePeriod = periodRepository.findByCompanyAndStatus(getSelectedUser().getCompany(), PeriodStatusEnum.ACTIVE);
		Assert.assertEquals(optActivePeriod.get().getId(), info.getActivePeriod().getId());
		// periodstatuses
		Assert.assertEquals(PeriodStatusEnum.values().length, info.getPeriodstatuses().size());
		// factors
		List<?> factors = factorRepository.findAllByActiveTrueAndAutomaticFalseAndCompanyOrderByName(getSelectedUser().getCompany());
		Assert.assertEquals(factors.size(), info.getFactors().size());

	}
}
