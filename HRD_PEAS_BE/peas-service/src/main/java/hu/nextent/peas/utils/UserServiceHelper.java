package hu.nextent.peas.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.constant.CommonConstants;
import hu.nextent.peas.security.services.AuthService;

@Service
@Transactional
public class UserServiceHelper {

    @Autowired
	private AuthService authService;
    
    @Autowired(required = false)
	private Optional<HttpServletRequest> optRequest;
    
    @Autowired
    private CompanyServiceHelper companyServiceHelper;
    
    public String getLanguage() {
		Supplier<String> _getRequestLanguage = this::getRequestLanguage;
		Supplier<String> _getUserLanguage = this::getUserLanguage;
		Supplier<String> _getDeaultLanguage = companyServiceHelper::getDeaultLanguage;
		List<Supplier<String>> queryLanguages = Arrays.asList(
			_getRequestLanguage, _getUserLanguage, _getDeaultLanguage
		);

		
		String language = null;
		for(Supplier<String> langGet : queryLanguages) {
			language = langGet.get();
			if (!StringUtils.isEmpty(language) && !"<string>".equals(language) ) {
				break;
			}
		}
		
		return language;
	}
    
    private String getRequestLanguage() {
    	String language = null;
    	if (optRequest.isPresent()) {
			language = optRequest.get().getHeader(CommonConstants.LANGUAGE_HEADER);
		}
    	if (StringUtils.isEmpty(language)) {
    		return language;
    	}
    	return language;
    }
    
    private String getUserLanguage() {
    	String language = null;
    	if (authService.currentUser() != null) {
			language = authService.currentUser().getLanguage();
		}
    	if (StringUtils.isEmpty(language)) {
    		return language;
    	}
    	return language;
    }
}
