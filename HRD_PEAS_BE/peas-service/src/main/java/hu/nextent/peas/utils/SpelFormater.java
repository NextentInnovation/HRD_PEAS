package hu.nextent.peas.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


public class SpelFormater {

	private static final ThreadLocal<ExpressionParser> PARSER_LOCAL =
			ThreadLocal.withInitial(() -> new SpelExpressionParser());
	
	private static final TemplateParserContext TEMPLATE_PARSER_CONTEXT = 
	            new TemplateParserContext("{{", "}}");   
	

    public static String format(
    		String expression, 
    		Object rootObject
    		) {
    	return format(expression, rootObject, null);
    }
	
    public static String format(
    		String expression, 
    		Object rootObject,
    		Map<String,String> labelGetTextResolver
    		) {
    	
    	if (StringUtils.isEmpty(expression)) {
    		return expression;
    	}
    	
    	StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
    	context.setVariables(SpelConstant.getConstantMap());
    	if (labelGetTextResolver != null) {
    		context.setVariable("label", labelGetTextResolver);
    	}
    	
    	try {
	        return PARSER_LOCAL
	        		.get()
	        		.parseExpression(
	        				expression,
	        				TEMPLATE_PARSER_CONTEXT
	        				)
	        		.getValue(context, String.class);
    	} catch (ParseException e) {
    		return String.format("Unparsable Exception: %s, message: %s", expression, e.getMessage());
    	}
    }
    
}
 