package hu.nextent.peas.resource.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.PeasAppInfoModel;
import hu.nextent.peas.service.ResourceService;

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
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller("hu.nextent.peas.resource.api.InfoV1Api")
@Api(value = "info_v1", description = "the info_v1 API")
public class InfoV1ApiController implements InfoV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public InfoV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Alkalmazás információk", nickname = "getInfo", notes = "Alkalmazás információk<br/>   * Currens felhasználó<br/>   * Vezető<br/>   * Nehézségi szintek<br/>   * Vállalati értékek<br/>   * Csoport értékek<br/>   * Task típusok<br/>   * Task státuszok<br/>   * Értékelési státuszok<br/>   * Értékelési szempontok<br/>   * Értékelési státuszok<br/>   * Értékelési szempontok<br/>   * Aktív periódus<br/>   * Periódus státuszok", response = PeasAppInfoModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Információk", response = PeasAppInfoModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/info_v1",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<PeasAppInfoModel> getInfo()
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getInfo();
        }
        return new ResponseEntity<PeasAppInfoModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	@Autowired
	ResourceService resourceService;
	
    public ResponseEntity<PeasAppInfoModel> inner_getInfo() {
        return resourceService.getInfo();
    }


   // ----------------------------------------------------------------------------------------

   
}



