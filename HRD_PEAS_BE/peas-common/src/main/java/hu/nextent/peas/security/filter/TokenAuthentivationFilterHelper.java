package hu.nextent.peas.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface TokenAuthentivationFilterHelper {

	void filter(HttpServletRequest request) throws ServletException, IOException;
}
