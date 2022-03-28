package hu.nextent.peas.service.resource;

import java.util.Map;

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
import hu.nextent.peas.jpa.dao.LabelRepository;
import hu.nextent.peas.service.ResourceService;
import hu.nextent.peas.service.TestServiceBase;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestGetAllLabel extends TestServiceBase {
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private LabelRepository labelRepository;
	
	@Test
	@Rollback
	public void testGetAllLAbelWithDefaultLanguage() {
		givenUser(TestConstant.USER_NORMAL);
		mockAuthService();
		
		ResponseEntity<Object> respLabels = resourceService.getAllLabel();
		
		Assert.assertNotNull(respLabels);
		Assert.assertEquals(respLabels.getStatusCode(), HttpStatus.OK);
		@SuppressWarnings("unchecked")
		Map<String, String> labels = (Map<String, String>)respLabels.getBody();
		Assert.assertNotNull(labels);
		Assert.assertTrue(!labels.isEmpty());
	}

}
