package hu.nextent.peas.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Assert;

public class ResponseEntityWriter {

	
	private final static ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();
	
	public static void writeTo(
			@NotNull HttpServletResponse response,
			ObjectMapper objectMapper,
			@NotNull ResponseEntity<?> responseEntity
			) throws IOException 
	{
		Assert.notNull(response);
		Assert.notNull(responseEntity);
		
		objectMapper = Optional.ofNullable(objectMapper).orElse(DEFAULT_OBJECT_MAPPER);
		
		for (Map.Entry<String, List<String>> header : responseEntity.getHeaders().entrySet()) {
	        String chave = header.getKey();
	        for (String valor : header.getValue()) {
	        	response.addHeader(chave, valor);                
	        }
	    }
		
		response.setStatus(responseEntity.getStatusCodeValue());
		if (responseEntity.hasBody()) {
			response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
		}
		
		response.setStatus(responseEntity.getStatusCodeValue());
		
	}
}
