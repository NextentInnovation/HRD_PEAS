package hu.nextent.peas.service.resource;

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
import hu.nextent.peas.model.AutocompletModel;
import hu.nextent.peas.model.AutocompletQueryModel;
import hu.nextent.peas.model.AutocompletQueryModel.AutocompletTypeEnum;
import hu.nextent.peas.service.ResourceService;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;
import lombok.extern.slf4j.Slf4j;

/*
 * Nem teljes teszt csak azt nézi, hogy lekérdezhető
 */
@Slf4j
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestAutoComplet extends TestServiceBase {
	
	@Autowired
	private ResourceService resourceService;
	
	@Test
	@Rollback
	public void testAutocompletTaskExists() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		for(AutocompletTypeEnum type: AutocompletTypeEnum.values()) {
			log.info("Check AutocompletTypeEnum: {}", type);
			exitsCheck(type);
		}
	}
	
	private void exitsCheck(AutocompletTypeEnum type) {
		AutocompletQueryModel queryModel = new AutocompletQueryModel();
		queryModel.autocompletType(type);
		
		ResponseEntity<AutocompletModel> respModel = resourceService.queryAutocomplet(queryModel);
		
		Assert.assertNotNull(respModel);
		Assert.assertEquals(respModel.getStatusCode(), HttpStatus.OK);
		AutocompletModel model = respModel.getBody();
		Assert.assertNotNull(model);
		Assert.assertNotNull(model.getContent());
		Assert.assertTrue(!model.getContent().isEmpty());
		Assert.assertTrue(model.getContent().size() > 0);
		Assert.assertTrue(model.getTotalElements() > 0);
		Assert.assertEquals(type.name(), model.getAutocompletType());
	}

}
