package hu.nextent.peas.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import hu.nextent.peas.model.PagingAndSortingModel;
import hu.nextent.peas.model.PagingAndSortingModel.OrderEnum;
import lombok.var;

public class PageableFactory {
	
	private static int DEFAULT_PAGE_SIZE = 10;
	
	private static PageableFactory instance = new PageableFactory();
	
	private static class Context {
		private PagingAndSortingModel pagingAndSorting;
		private Map<String, String> renameFields;
		private Integer defaultPageSize;
		
		private Integer pageNumber;
		private Integer pageSize;
		private Order sorting;
	}

	
	void applyPageNumber(Context context) {
		context.pageNumber = context.pagingAndSorting == null ? null : context.pagingAndSorting.getNumber();
		context.pageNumber = context.pageNumber == null ? 0 : context.pageNumber;
	}

	void applyPageSize(Context context) {
		context.pageSize = context.pagingAndSorting == null ? null : context.pagingAndSorting.getSize();
		context.pageSize = context.pageSize == null ? context.defaultPageSize : context.pageSize;
		context.pageSize = context.pageSize == null ? DEFAULT_PAGE_SIZE : context.pageSize;
	}
	
	void applySort(Context context) {
		
		if (context.pagingAndSorting == null) {
			return;
		}
		
		var property = context.pagingAndSorting.getSort();
		var direction = context.pagingAndSorting.getOrder();
		
		
		if (StringUtils.isNotEmpty(property)) {
			if (context.renameFields != null && context.renameFields.containsKey(property)) {
				property = context.renameFields.get(property);
			}
			
			if (direction == null || direction.equals(OrderEnum.ASC)) {
				context.sorting = Order.asc(property);
			} else {
				context.sorting = Order.desc(property);
			}
		}
	}
	
	public Pageable build(Context context) {
		if (context.sorting == null) {
			return PageRequest.of(context.pageNumber, context.pageSize);
		}
		return PageRequest.of(context.pageNumber, context.pageSize, Sort.by(context.sorting));
	}
	
	
	
	public static Pageable build(
			Integer defaultPageSize, 
			Map<String, String> renameFields, 
			PagingAndSortingModel pagingAndSorting
			) 
	{
		Context context = new Context();
		context.pagingAndSorting = pagingAndSorting;
		context.renameFields = renameFields;
		context.defaultPageSize = defaultPageSize;
		instance.applyPageSize(context);
		instance.applyPageNumber(context);
		instance.applySort(context);
		return instance.build(context);
	}
	
	public static Pageable build(
			String defaultPageSize, 
			Map<String, String> renameFields, 
			PagingAndSortingModel pagingAndSorting
			) 
	{
		return build(
				StringUtils.isEmpty(defaultPageSize) ? DEFAULT_PAGE_SIZE : Integer.valueOf(defaultPageSize),
				renameFields,
				pagingAndSorting
				);
	}
}
