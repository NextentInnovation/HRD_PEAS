package hu.nextent.peas.auth.api;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.nextent.peas.auth.service.ApiAuthServiceImpl;
import hu.nextent.peas.model.AuthenticationRequestModel;
import hu.nextent.peas.model.ErrorMessageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller("hu.nextent.peas.auth.api.LoginV1Api")
@Api(value = "login_v1", description = "the login_v1 API")
public class LoginV1ApiController implements LoginV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Bejelentkezés PEAS user-rel", nickname = "login", notes = "", response = String.class, tags={ "auth", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Autentikációs JWT token, amelyet viszaad eredményben is és header-ben is", response = String.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/login_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    public ResponseEntity<String> login(@ApiParam(value = "Felhasználónév-jelszó" ,required=true )  @Valid @RequestBody AuthenticationRequestModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_login(body);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Virtualis felhasználó esetés token alapú bejelentkezés", nickname = "virtualUserLogin", notes = "Levélben meghívott felhasználó beléptetése", response = String.class, tags={ "auth", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Autentikációs JWT token", response = String.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/login_v1/{virtualUserToken}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<String> virtualUserLogin(@ApiParam(value = "User Token",required=true) @PathVariable("virtualUserToken") String virtualUserToken)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_virtualUserLogin(virtualUserToken);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }
 

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
    @Autowired
    ApiAuthServiceImpl apiAuthService;
	
    public ResponseEntity<String> inner_login( AuthenticationRequestModel  body) {
        return apiAuthService.login(body);
    }

    public ResponseEntity<String> inner_virtualUserLogin( String  virtualUserToken) {
        return apiAuthService.virtualUserLogin(virtualUserToken);
    }


   // ----------------------------------------------------------------------------------------

   
}



