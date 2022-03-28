package hu.nextent.peas.converters;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

public abstract class AbstractConverter <S, T>
implements Converter<S, T>, InitializingBean {

	@Autowired
	protected ConverterRegistry converterRegistry;
	
	@Autowired
	protected ConversionService conversionService;

	@Override
	public void afterPropertiesSet() throws Exception {
		converterRegistry.addConverter(this);
	}
}
