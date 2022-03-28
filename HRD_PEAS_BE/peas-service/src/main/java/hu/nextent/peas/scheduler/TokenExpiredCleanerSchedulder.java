package hu.nextent.peas.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.dao.TokenCacheRespository;
import hu.nextent.peas.jpa.entity.TokenCache;
import hu.nextent.peas.jpa.entity.TokenCache_;
import hu.nextent.peas.jpa.filter.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Torli a lejárt token-eket
 * @author peter.tamas
 *
 */
@Slf4j
@Component
@Transactional
public class TokenExpiredCleanerSchedulder {
	
	private static final int MAX_SIZE = 100;
	
	@Autowired
	private TokenCacheRespository tokenCacheRespository;

	@Scheduled(fixedDelay = 3600000, initialDelay = 900000) // óránként
	public void scheduleTokenExpiredCleaner() {
		log.debug("scheduleTokenExpiredCleaner");
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		
		Specification<TokenCache> spec = SpecificationFactory.lt(TokenCache_.EXP, now);
		long cnt = tokenCacheRespository.count(spec);
		log.debug("Founded {} expired tokens", cnt);
		
		Pageable pageable = PageRequest.of(0, MAX_SIZE);
		for(;;) { 
			Page<TokenCache> expiredTokens = 
					tokenCacheRespository.findAll(spec, pageable);
			
			if (expiredTokens.getNumberOfElements() == 0) {
				break;
			}

			tokenCacheRespository.deleteAll(expiredTokens.getContent());
			tokenCacheRespository.flush();
		}
	}
	
}
