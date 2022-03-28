package hu.nextent.peas.jpa.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.util.StringUtils;

import hu.nextent.peas.jpa.filter.Criterion.Operator;

public class ExpressionFactory {

    public static SimpleExpression eq(String fieldName, Object value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.EQ, value);  
    }  

    public static SimpleExpression ne(String fieldName, Object value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.NE, value);  
    }  
  
    public static SimpleExpression like(String fieldName, String value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.LIKE, value);  
    }
    
    public static SimpleExpression ilike(String fieldName, String value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.ILIKE, value);  
    }  

  
    /** 
     *  
     * @param fieldName 
     * @param value 
     * @param matchMode 
     * @param ignoreNull 
     * @return 
     */  
    /*
    public static SimpleExpression like(String fieldName, String value, MatchMode matchMode, boolean ignoreNull) {  
        if(StringUtils.isEmpty(value))return null;  
        return null;  
    } 
    */ 
  
    public static SimpleExpression gt(String fieldName, Object value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.GT, value);  
    }  
  
    public static SimpleExpression lt(String fieldName, Object value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.LT, value);  
    }  

    public static SimpleExpression lte(String fieldName, Object value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.LTE, value);  
    }  
  
    public static SimpleExpression gte(String fieldName, Object value) {  
        if(StringUtils.isEmpty(value))return null;  
        return new SimpleExpression(fieldName, Operator.GTE, value);  
    }

    public static <T> SimpleExpression between (String fieldName, T min, T max) {
    	if (min == null && max == null) {
    		return null;
    	} else if (min != null && max != null) {
    		return new SimpleExpression(fieldName, Operator.BETWEEN, min, max);
    	} else if (min != null && max == null) {
    		return new SimpleExpression(fieldName, Operator.GTE, min);  
    	} else if (min == null && max != null) {
    		return new SimpleExpression(fieldName, Operator.LTE, max);  
    	}
    	
    	return null;
    }

    public static SimpleExpression isNull(String fieldName) {  
        return new SimpleExpression(fieldName, Operator.IS_NULL);  
    }  
  
    public static SimpleExpression isNotNull(String fieldName) {  
        return new SimpleExpression(fieldName, Operator.IS_NOT_NULL);  
    }
    
    public static SimpleExpression isEmpty(String fieldName) {  
        return new SimpleExpression(fieldName, Operator.IS_EMPTY);  
    }  
  
    public static SimpleExpression isNotEmpty(String fieldName) {  
        return new SimpleExpression(fieldName, Operator.IS_NOT_EMPTY);  
    }
    
    public static LogicalExpression and(Criterion... criterions){ 
    	if (criterions == null) {
    		return null;
    	}
    	criterions = filterNull(criterions);
    	if (criterions.length == 0) {
    		return null;
    	}
        return new LogicalExpression(criterions, Operator.AND);  
    }  

    public static LogicalExpression or(Criterion... criterions){  
    	if (criterions == null) {
    		return null;
    	}
    	criterions = filterNull(criterions);
    	if (criterions.length == 0) {
    		return null;
    	}
        return new LogicalExpression(criterions, Operator.OR);  
    }  
    
    private static Criterion[] filterNull(Criterion[] criterions) {
    	return Arrays.stream(criterions)
                .filter(s -> !Objects.isNull(s))
                .distinct()
                .toArray(Criterion[]::new);    
    }

    @SuppressWarnings("rawtypes")  
    public static LogicalExpression in(String fieldName, Collection value) {  
        if(value == null || value.isEmpty()){  
            return null;  
        }  
        List<SimpleExpression> ses = new ArrayList<SimpleExpression>();
        for(Object obj : value) {
        	if (value == null) {
        		continue;
        	}
            ses.add(new SimpleExpression(fieldName,Operator.EQ, obj));  
        }
        
        if (ses.isEmpty()) {
        	return null;
        }
        
        return new LogicalExpression(ses.toArray(new Criterion[0]),Operator.OR);  
    }  
}
