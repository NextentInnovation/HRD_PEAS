package hu.nextent.peas.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.config.HibernateConfig;
import hu.nextent.peas.jpa.config.HibernateTestConfig;
import hu.nextent.peas.jpa.dao.SystemParameterRepository;
import hu.nextent.peas.jpa.entity.SystemParameter;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = { HibernateTestConfig.class, HibernateConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestSystemParameterRepository {

	@Resource
	private SystemParameterRepository systemParameterRepository;

	private static final String CODE = "page.size";

	@Test
	public void givenKeyword() {
		Optional<SystemParameter> param = systemParameterRepository.findByCode(CODE);
		assertTrue("Not Founded", param.isPresent());
		assertEquals("code incorrect", CODE, param.get().getCode());
	}

}
