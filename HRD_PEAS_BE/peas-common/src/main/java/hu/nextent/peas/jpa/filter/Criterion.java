package hu.nextent.peas.jpa.filter;

import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface Criterion {

    public enum Operator {  
        EQ, NE, LIKE, ILIKE, GT, LT, GTE, LTE, BETWEEN, AND, OR, IS_NULL, IS_NOT_NULL, IS_EMPTY, IS_NOT_EMPTY
    }  
    
    public Predicate toPredicate(Root<?> root, CommonAbstractCriteria query, CriteriaBuilder builder);  
}
