package hu.nextent.peas.auth.api;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.nextent.peas.auth.service.ApiAuthServiceImpl;
import hu.nextent.peas.model.ErrorMessageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Controller("hu.nextent.peas.auth.api.LogoutV1Api")
@Api(value = "logout_v1", description = "the logout_v1 API")
public class LogoutV1ApiController implements LogoutV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LogoutV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }



    @ApiOperation(value = "Kilépés PEAS userrel WEB és APP felhasználóval", nickname = "logout", notes = "", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "auth", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/logout_v1",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    public ResponseEntity<Void> logout()
	{
        if (getAcceptHeader().isPresent()) {
            return inner_logout();
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
    @Autowired
    ApiAuthServiceImpl apiAuthService;
	
    public ResponseEntity<Void> inner_logout() {
        return apiAuthService.logout();
    }


   // ----------------------------------------------------------------------------------------

   
}



