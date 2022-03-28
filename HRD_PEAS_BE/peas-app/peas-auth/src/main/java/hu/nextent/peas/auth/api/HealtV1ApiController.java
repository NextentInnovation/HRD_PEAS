package hu.nextent.peas.auth.api;

import hu.nextent.peas.auth.service.ApiAuthServiceImpl;
import hu.nextent.peas.model.ErrorMessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;

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

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller("hu.nextent.peas.auth.api.HealtV1Api")
@Api(value = "healt_v1", description = "the healt_v1 API")
public class HealtV1ApiController implements HealtV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public HealtV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Jelzi, hogy él a az alkalmazás", nickname = "healthly", notes = "Felhő, LW vagy egyéb esetben ezt a szolgáltatást lehet beregisztrálni, hogy jelezze, hogy az alkalmazás él.", tags={ "base","auth", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Live"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/healt_v1",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<Void> healthly()
	{
        if (getAcceptHeader().isPresent()) {
            return inner_healthly();
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
	
    public ResponseEntity<Void> inner_healthly() {
        return apiAuthService.healthly(request);
    }


   // ----------------------------------------------------------------------------------------

   
}



