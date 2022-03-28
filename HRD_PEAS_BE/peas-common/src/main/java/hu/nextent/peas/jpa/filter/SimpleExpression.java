package hu.nextent.peas.jpa.filter;


import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

public class SimpleExpression implements Criterion {  
    
  private String fieldName;
  private Object[] values; 
  private Operator operator;
  
  
  @SafeVarargs
  protected SimpleExpression(String fieldName, Operator operator, Object ... values) {
      this.fieldName = fieldName;  
      this.values = values;  
      this.operator = operator;  
  }  

  public String getFieldName() {  
      return fieldName;  
  }  
  public Object[] getValues() {  
      return values;  
  }  

  public Operator getOperator() {  
      return operator;  
  }  
  @SuppressWarnings({ "rawtypes", "unchecked" })  
  public Predicate toPredicate(Root<?> root, CommonAbstractCriteria query, CriteriaBuilder builder) { 
      Path expression = null;  
      if(fieldName.contains(".")){  
          String[] names = StringUtils.split(fieldName, ".");  
          expression = root.get(names[0]);  
          for (int i = 1; i < names.length; i++) {  
              expression = expression.get(names[i]);  
          }  
      }else{  
          expression = root.get(fieldName);  
      }  
      
      switch (operator) {  
      case EQ:  
          return builder.equal(expression, values[0]);  
      case NE:  
          return builder.notEqual(expression, values[0]);  
      case LIKE:  
          return builder.like((Expression<String>) expression, "%" + values[0] + "%");  
      case ILIKE:
    	  String val = null;
    	  if (values[0] instanceof String) {
    		  val = (String)values[0];
    	  } else {
    		  val = values[0].toString();
    	  }
          return builder.like((Expression<String>) expression, "%" + val.toLowerCase() + "%");  
      case LT:  
          return builder.lessThan(expression, (Comparable)values[0]);  
      case GT:  
          return builder.greaterThan(expression, (Comparable)values[0]);  
      case LTE:  
          return builder.lessThanOrEqualTo(expression, (Comparable)values[0]);  
      case GTE:  
          return builder.greaterThanOrEqualTo(expression, (Comparable)values[0]);  
      case IS_NULL:
    	  return builder.isNull(expression);
      case IS_NOT_NULL:
    	  return builder.isNotNull(expression);
      case IS_EMPTY:
    	  return builder.isEmpty(expression);
      case IS_NOT_EMPTY:
    	  return builder.isNotEmpty(expression);
      case BETWEEN:
    	  return builder.between(expression, (Comparable)values[0], (Comparable)values[1]);
      default:  
          return null;  
      }  
  }  
    
} 
