package hu.nextent.peas.demo.api;

import hu.nextent.peas.model.ErrorMessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller("hu.nextent.peas.demo.api.HelloApi")
@Api(value = "hello", description = "the hello API")
public class HelloApiController implements HelloApi {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public HelloApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Hello-t mondó hívás", nickname = "helloSomeOne", notes = "name helyére egy nevet beírva, köszön az alkalmazás", response = String.class, tags={ "demo", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Say hello!", response = String.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/hello/{name}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<String> helloSomeOne(@ApiParam(value = "Name from FE",required=true) @PathVariable("name") String name)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_helloSomeOne(name);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    
	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	
    public ResponseEntity<String> inner_helloSomeOne( String  name) {
        return new ResponseEntity<String>("Hello: " + name, HttpStatus.OK);
    }


   // ----------------------------------------------------------------------------------------

   
}



