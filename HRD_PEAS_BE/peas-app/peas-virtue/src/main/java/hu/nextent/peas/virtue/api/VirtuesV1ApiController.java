package hu.nextent.peas.virtue.api;

import hu.nextent.peas.model.CompanyVirtueEditableModel;
import hu.nextent.peas.model.CompanyVirtueEditablePageModel;
import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.service.VirtueService;
import hu.nextent.peas.model.CompanyVirtueQueryParameterModel;

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

@Controller("hu.nextent.peas.virtue.api.VirtuesV1Api")
@Api(value = "virtues_v1", description = "the virtues_v1 API")
public class VirtuesV1ApiController implements VirtuesV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public VirtuesV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Új szerkeszthető céges érték létrehozása", nickname = "createCompanyVirtue", notes = "Új szerkeszthető céges érték létrehozása Csak ADMIN és HR jogkörrel Csak az editablevalue lesz mentve. Akkor jelenik meg más felületetn, ha a value értéke nem null", response = CompanyVirtueEditableModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Céges értékek lekérdezése", response = CompanyVirtueEditableModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    public ResponseEntity<CompanyVirtueEditableModel> createCompanyVirtue(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CompanyVirtueEditableModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_createCompanyVirtue(body);
        }
        return new ResponseEntity<CompanyVirtueEditableModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy szerkeszthető céges érték lekérdezése", nickname = "getEditableCompanyVirtue", notes = "Egy szerkeszthető céges érték lekérdezése", response = CompanyVirtueEditableModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Egy szerkeszthető céges érték", response = CompanyVirtueEditableModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/{virtueId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue(@ApiParam(value = "Kiválasztott céges érték",required=true) @PathVariable("virtueId") Long virtueId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getEditableCompanyVirtue(virtueId);
        }
        return new ResponseEntity<CompanyVirtueEditableModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy szerkeszthető céges érték módosítása", nickname = "modifyEditableCompanyVirtue", notes = "Egy szerkeszthető céges érték módosítása Csak az editvalue lesz módosítva", response = CompanyVirtueEditableModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Céges értékek lekérdezése", response = CompanyVirtueEditableModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/{virtueId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<CompanyVirtueEditableModel> modifyEditableCompanyVirtue(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CompanyVirtueEditableModel body,@ApiParam(value = "Kiválasztott céges érték",required=true) @PathVariable("virtueId") Long virtueId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_modifyEditableCompanyVirtue(body, virtueId);
        }
        return new ResponseEntity<CompanyVirtueEditableModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy szerkeszthető céges érték publikálása", nickname = "publicEditableCompanyVirtue", notes = "Egy szerkeszthető céges érték publikálása Ha van editvalue, akkor azt a value helyére másolja", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Céges értékek publikálása"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/public/{virtueId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    public ResponseEntity<Void> publicEditableCompanyVirtue(@ApiParam(value = "Kiválasztott céges érték",required=true) @PathVariable("virtueId") Long virtueId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_publicEditableCompanyVirtue(virtueId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Céges értékek lekérdezése", nickname = "queryEditableCompanyVirtues", notes = "Szerkeszthető Céges értékek lekérdezése", response = CompanyVirtueEditablePageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "virtue", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Szerkeszthető céges értékek lekérdezése", response = CompanyVirtueEditablePageModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<CompanyVirtueEditablePageModel> queryEditableCompanyVirtues(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CompanyVirtueQueryParameterModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_queryEditableCompanyVirtues(body);
        }
        return new ResponseEntity<CompanyVirtueEditablePageModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	@Autowired private VirtueService virtueService;
	
    public ResponseEntity<CompanyVirtueEditableModel> inner_createCompanyVirtue( CompanyVirtueEditableModel  body) {
        return virtueService.createCompanyVirtue(body);
    }
    public ResponseEntity<CompanyVirtueEditableModel> inner_getEditableCompanyVirtue( Long  virtueId) {
        return virtueService.getEditableCompanyVirtue(virtueId);
    }
    public ResponseEntity<CompanyVirtueEditableModel> inner_modifyEditableCompanyVirtue( CompanyVirtueEditableModel  body, Long  virtueId) {
        return virtueService.modifyEditableCompanyVirtue(body, virtueId);
    }
    public ResponseEntity<Void> inner_publicEditableCompanyVirtue( Long  virtueId) {
        return virtueService.publicEditableCompanyVirtue(virtueId);
    }
    public ResponseEntity<CompanyVirtueEditablePageModel> inner_queryEditableCompanyVirtues( CompanyVirtueQueryParameterModel  body) {
    	return virtueService.queryCompanyVirtues(body);
    }


   // ----------------------------------------------------------------------------------------

   

}



