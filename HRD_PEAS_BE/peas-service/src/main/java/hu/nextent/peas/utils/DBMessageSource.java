package hu.nextent.peas.utils;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import hu.nextent.peas.cache.ServiceCaches;

/**
 * Label Táblából olvassa az adatokat a MessageSource számára
 * @author Peter.Tamas
 *
 */
@Component("messageSource")
public class DBMessageSource extends AbstractMessageSource {

	@Autowired
	private ServiceCaches serviceCaches;
	
	@Override
    protected MessageFormat resolveCode(String key, Locale locale) {
        String message = serviceCaches.getLabel(key, locale == null ? null : locale.getLanguage());
        return new MessageFormat(message, locale);
    }
	
	
	
}
