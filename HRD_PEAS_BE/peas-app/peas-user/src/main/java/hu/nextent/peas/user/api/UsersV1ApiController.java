package hu.nextent.peas.user.api;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.UserPageModel;
import hu.nextent.peas.model.UserQueryParameterModel;
import hu.nextent.peas.service.UserService;

@Controller("hu.nextent.peas.user.api.UsersV1Api")
@Api(value = "users_v1", description = "the users_v1 API")
public class UsersV1ApiController implements UsersV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Felhasználók lekérdezése kereső paraméterekkel", nickname = "getAllUser", notes = "Felhasználók lekérdezése kereső paraméterekkel", response = UserPageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Felhasználók adatai Ha a queryType normal, akkor teljes információt adunk vissza a felhasználóról Ha a queryType simple, akkor egyszerűsített információt adunk vissza a felhasználóról", response = UserPageModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/users_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<UserPageModel> getAllUser(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UserQueryParameterModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getAllUser(body);
        }
        return new ResponseEntity<UserPageModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------

    @Autowired
    private UserService userService;
	
    public ResponseEntity<UserPageModel> inner_getAllUser( UserQueryParameterModel  body) {
    	return (ResponseEntity<UserPageModel>)userService.getAllUser(body);
    }

   // ----------------------------------------------------------------------------------------

   
}



