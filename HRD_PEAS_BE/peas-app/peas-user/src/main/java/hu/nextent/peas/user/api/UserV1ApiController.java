package hu.nextent.peas.user.api;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import hu.nextent.peas.model.UserModel;
import hu.nextent.peas.model.UserPageModel;
import hu.nextent.peas.service.UserService;

@Controller("hu.nextent.peas.user.api.UserV1Api")
@Api(value = "user_v1", description = "the user_v1 API")
public class UserV1ApiController implements UserV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    
    @org.springframework.beans.factory.annotation.Autowired
    public UserV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Egy felhasználó lekérdezése", nickname = "getUser", notes = "Egy felhasználó lekérdezése<br/> Itt egy adott felhasználó teljes objektumként adunk vissza", response = UserModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Felhasználó adata", response = UserModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/user_v1/{userId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<UserModel> getUser(@ApiParam(value = "Kiválasztott felhasználó azonosító",required=true) @PathVariable("userId") Long userId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getUser(userId);
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
    private UserService userService;
	
	
    public ResponseEntity<UserModel> inner_getUser( Long  userId) {
    	return userService.getUser(userId);
    }


   // ----------------------------------------------------------------------------------------

   
}



