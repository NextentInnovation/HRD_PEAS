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
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.User;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = { HibernateTestConfig.class, HibernateConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestUserRepository {

	@Resource
	private UserRepository userRepository;

	private static final String USER_NAME = "hegedus.tamas";
	private static final String COMPANY_NAME = "nextent";

	@Test
	public void givenUser() {
		Optional<User> user = userRepository.findByUserNameAndCompany_Name(USER_NAME, COMPANY_NAME);
		assertTrue("Not Founded", user.isPresent());
		assertEquals("name incorrect", USER_NAME, user.get().getUserName());
		System.out.print(user);
	}

}
