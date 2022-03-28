package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.TestConstant;
import hu.nextent.peas.jpa.dao.TokenCacheRespository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.TokenCache;
import hu.nextent.peas.jpa.entity.User;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestSchedulerConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestTokenExpiredCleanerSchedulder {

	private static final String SUBJECT = "subject";
	private static final String ISSUE = "ISSUE";
	
	@Autowired
	private TokenCacheRespository tokenCacheRespository;

	@Autowired
	private UserRepository userRespository;

	@Autowired
	private TokenExpiredCleanerSchedulder testedSchedulder;
	
	private User user;
	
	private void givenUser() {
		user = userRespository.findByUserNameAndCompany_Name(TestConstant.USER_NORMAL, TestConstant.DEFAULT_COMPANY).get();
	}
	
	private void prepareTokens() {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime expiredDate = now.minusMinutes(1); 
		// 10 lejárt toke
		for(int i=0;i<10;i++) {
			TokenCache tokenCache = 
					TokenCache.builder()
						.user(user)
						.exp(expiredDate)
						.sub(SUBJECT)
						.iss(ISSUE)
						.build();
			tokenCacheRespository.save(tokenCache);
		}
		
		OffsetDateTime notExpiredDate = now.plusMinutes(10);
		// 10 élő toke
		for(int i=0;i<10;i++) {
			TokenCache tokenCache = 
					TokenCache.builder()
						.user(user)
						.exp(notExpiredDate)
						.sub(SUBJECT)
						.iss(ISSUE)
						.build();
			tokenCacheRespository.save(tokenCache);
		}
		tokenCacheRespository.flush();
	}
	
	@Test
	@Rollback
	public void testTokenClean() {
		givenUser();
		prepareTokens();
		
		testedSchedulder.scheduleTokenExpiredCleaner();
		
		long count = tokenCacheRespository.count();
		Assert.assertEquals(10L, count);
	}
}
