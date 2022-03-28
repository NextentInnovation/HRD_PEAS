package hu.nextent.peas.resource.api;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import javax.validation.constraints.*;
import javax.validation.Valid;
import java.util.Optional;


import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.service.ResourceService;

@Controller("hu.nextent.peas.resource.api.LabelV1Api")
@Api(value = "label_v1", description = "the label_v1 API")
public class LabelV1ApiController implements LabelV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LabelV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Címkék lekérdezése", nickname = "getAllLabel", notes = "Címkék lekérdezése", response = Object.class, tags={ "resource","label", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Címkék", response = Object.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/label_v1",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<Object> getAllLabel()
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getAllLabel();
        }
        return new ResponseEntity<Object>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    
	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	@Autowired
	ResourceService resourceService;
	
    public ResponseEntity<Object> inner_getAllLabel() {
        return resourceService.getAllLabel();
    }



   // ----------------------------------------------------------------------------------------

   
}



