package hu.nextent.peas.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.util.ReflectionUtils;

import hu.nextent.peas.jpa.entity.base.JpaEntity;
import hu.nextent.peas.model.PageModel;

public class PageModelConverter 
	<T extends PageModel,  		// PageModel Class
	 SI extends JpaEntity<?>, 	// Source Entity Type
	 ST extends Serializable> 	// Target Item Type
{

	private Class<T> pageModelClass;
	private Function<SI, ST> converter;
	private Page<SI> page;
	
	private T pageModel;
	
	public PageModelConverter() {
	}
	
	public PageModelConverter<T, SI, ST> applyPageModelClass(Class<T> pageModelClass) {
		this.pageModelClass = pageModelClass;
		return this;
	}
	
	public PageModelConverter<T, SI, ST> applyConverter(Function<SI, ST> converter) {
		this.converter = converter;
		return this;
	}
	
	public PageModelConverter<T, SI, ST> applyPage(Page<SI> page) {
		this.page = page;
		return this;
	}
	
	public T apply() {
		try {
			createPageModel();
			setPageAttributes();
			setContent();
			return pageModel;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createPageModel() throws InstantiationException, IllegalAccessException {
		pageModel = pageModelClass.newInstance();
	}
	
	private void setPageAttributes() {
		pageModel.setNumber(page.getNumber());
		pageModel.setSize(page.getSize());
		pageModel.setTotalElements(page.getTotalElements());
		pageModel.setTotalPages(page.getTotalPages());
	}
	
	@SuppressWarnings("unchecked")
	private void setContent() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<SI> sourceContent = page.getContent();
		List<ST> targetContent = Collections.emptyList();
		if (sourceContent == null || sourceContent.isEmpty()) {
			
		} else {
			targetContent = 
				converter == null ?
						(List<ST>)sourceContent
						: sourceContent.stream().map(converter).collect(Collectors.toList());
		}
		
		Field field = ReflectionUtils.findRequiredField(pageModelClass, "content");
		ReflectionUtils.setField(field, pageModel, targetContent);
		
		;
	}
}
