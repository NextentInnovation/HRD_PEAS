package hu.nextent.peas.service.other;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.SystemParameterRepository;
import hu.nextent.peas.jpa.entity.SystemParameter;
import hu.nextent.peas.service.TestServiceConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServiceConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestPageable {

	@Autowired
	private SystemParameterRepository systemParameterRepository;
	
	@Test
	public void testOnePage() {
		
		long cnt = systemParameterRepository.count();
		Pageable pageable = PageRequest.of(0, Long.valueOf(cnt).intValue());
		
		Page<SystemParameter> onePage = systemParameterRepository.findAll(pageable);
		
		Assert.assertEquals(onePage.getTotalElements(), cnt);
		Assert.assertEquals(onePage.getNumberOfElements(), cnt);
		Assert.assertEquals(onePage.getNumber(), 0);
		
	}
	
	@Test
	public void testOnePageOver() {
		/*
		 * Amit meg akartam tudni, hogy mi lesz, ha túlkérdezek az elérhető elemeken
		 */
		long cnt = systemParameterRepository.count();
		Pageable pageable = PageRequest.of(1, Long.valueOf(cnt).intValue());
		
		Page<SystemParameter> onePage = systemParameterRepository.findAll(pageable);
		
		Assert.assertEquals(onePage.getTotalElements(), cnt);
		Assert.assertEquals(onePage.getNumberOfElements(), 0);
		Assert.assertEquals(onePage.getNumber(), 1);
		
		/*
		 * Amit megtudtam, hogy a túlkérdezés során egy üres lapot ad vissza
		 */
	}
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testOnePageUnder() {
		/*
		 * Amit meg akartam tudni, hogy mi lesz, ha alá kérdezek az elérhető lapok a számának
		 */
		thrown.expect(IllegalArgumentException.class);

		Pageable pageable = PageRequest.of(-1, 10);
		
		
	}
	
}
