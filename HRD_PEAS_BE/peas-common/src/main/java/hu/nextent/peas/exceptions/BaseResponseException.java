package hu.nextent.peas.exceptions;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class BaseResponseException 
extends NestedRuntimeException 
{

	private static final long serialVersionUID = -5645586165384725432L;
	
	private HttpStatus status;
	private String reasonCode;
	private Object[] args;

	public BaseResponseException(
			HttpStatus status, 
			@Nullable String reasonCode, 
			@Nullable Throwable cause, 
			Object ... args
			) 
	{
		super(reasonCode, cause);
		this.status = status;
		this.reasonCode = reasonCode;
		this.args = args;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	
	public String getReasonCode() {
		return reasonCode;
	}

	public Object[] getArgs() {
		return args;
	}

	public boolean hasArgs() {
		return args != null && args.length > 0;
	}

}
