package hu.nextent.peas.jpa.filter;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import hu.nextent.peas.jpa.entity.base.JpaEntity;

@SuppressWarnings("serial")
public class SpecificationFactory {

	
	public static <T extends JpaEntity<?>> Specification<T> eq(String fieldName, Object value) {
		if (value == null) {
			return null;
		}
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.eq(fieldName, value).toPredicate(root, query, builder);
			}
		};
				
	}
	
	public static <T extends JpaEntity<?>> Specification<T> ne(String fieldName, Object value) {  
		if (value == null) {
			return null;
		}
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.ne(fieldName, value).toPredicate(root, query, builder);
			}
		};
    }  

	public static <T extends JpaEntity<?>> Specification<T> like(String fieldName, String value) {  
        if(StringUtils.isEmpty(value))return null;
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.like(fieldName, value).toPredicate(root, query, builder);
			}
		};
    }
    
	public static <T extends JpaEntity<?>> Specification<T> ilike(String fieldName, String value) {  
    	if(StringUtils.isEmpty(value))return null;
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.like(fieldName, value).toPredicate(root, query, builder);
			}
		};
    }  

	public static <T extends JpaEntity<?>> Specification<T> gt(String fieldName, Object value) {  
    	if (value == null) {
			return null;
		}
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.gt(fieldName, value).toPredicate(root, query, builder);
			}
		};
    }  
	
	public static <T extends JpaEntity<?>> Specification<T> gte(String fieldName, Object value) {  
    	if (value == null) {
			return null;
		}
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.gte(fieldName, value).toPredicate(root, query, builder);
			}
		};
    }  
	
	public static <T extends JpaEntity<?>> Specification<T> lt(String fieldName, Object value) {  
    	if (value == null) {
			return null;
		}
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.lt(fieldName, value).toPredicate(root, query, builder);
			}
		};
    }  
  
	public static <T extends JpaEntity<?>> Specification<T> lte(String fieldName, Object value) {  
    	if (value == null) {
			return null;
		}
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.lte(fieldName, value).toPredicate(root, query, builder);
			}
		};
    } 
	
	public static <T extends JpaEntity<?>> Specification<T> in(String fieldName, Collection<?> value) {  
		if(value==null||value.isEmpty()){  
            return null;  
        }  
        return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return ExpressionFactory.in(fieldName, value).toPredicate(root, query, builder);
			}
		};
    }  
	
	
	public static <T extends JpaEntity<?>> Specification<T> build(List<Specification<T>> specifications) {
		Specification<T> result = null;
		boolean first = true;
		for (Specification<T> spec: specifications) {
			if (first) {
				result = Specification.where(spec);
			}
			result.and(spec);
		}
		return result;
	}
	
	public static <T extends JpaEntity<?>> Specification<T> make(Criterion criterion) {
		return (root,query,builder) -> {
				return criterion.toPredicate(root, query, builder);
			};
	}
	 	
}
