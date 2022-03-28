package hu.nextent.peas.jpa.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/** 
* @author lee 
* 
*/  
public class LogicalExpression implements Criterion {  
    private Criterion[] criterion;  
    private Operator operator;  
  
    public LogicalExpression(Criterion[] criterions, Operator operator) {  
        this.criterion = criterions;  
        this.operator = operator;  
    }  
  
    public Predicate toPredicate(Root<?> root, CommonAbstractCriteria query, CriteriaBuilder builder) {  
        List<Predicate> predicates = new ArrayList<Predicate>();  
        for(int i=0;i<this.criterion.length;i++){
        	if (this.criterion[i] != null) {
        		predicates.add(this.criterion[i].toPredicate(root, query, builder));
        	}
        }  
        switch (operator) {  
        case OR:  
            return builder.or(predicates.toArray(new Predicate[predicates.size()]));  
        default:  
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));  
        }  
    }  
  
}  