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
import hu.nextent.peas.model.UserModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Controller("hu.nextent.peas.auth.api.MeV1Api")
@Api(value = "me_v1", description = "the me_v1 API")
public class MeV1ApiController implements MeV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public MeV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Kurrens PEAS felhasználó lekérdezése", nickname = "currentUser", notes = "Kurrens PEAS felhasználó lekérdezése Több nem kell, mert LDAP alapján megy a felhasználó kezelés Ez kliens oldalt segítő funkció, hogy le tudja kérdezni a felhasználó információkat", response = UserModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "auth", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Kurrens felhasználó adatai", response = UserModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/me_v1",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<UserModel> currentUser()
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_currentUser();
        }
        return new ResponseEntity<UserModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------

    @Autowired
    ApiAuthServiceImpl apiAuthService;

	
    public ResponseEntity<UserModel> inner_currentUser() {
        return apiAuthService.currentUserModel();
    }


   // ----------------------------------------------------------------------------------------

   
}



