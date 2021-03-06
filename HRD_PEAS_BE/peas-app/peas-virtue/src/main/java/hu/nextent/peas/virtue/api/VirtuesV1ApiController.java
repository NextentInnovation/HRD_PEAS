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



    @ApiOperation(value = "??j szerkeszthet?? c??ges ??rt??k l??trehoz??sa", nickname = "createCompanyVirtue", notes = "??j szerkeszthet?? c??ges ??rt??k l??trehoz??sa Csak ADMIN ??s HR jogk??rrel Csak az editablevalue lesz mentve. Akkor jelenik meg m??s fel??letetn, ha a value ??rt??ke nem null", response = CompanyVirtueEditableModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "C??ges ??rt??kek lek??rdez??se", response = CompanyVirtueEditableModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
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
    

    @ApiOperation(value = "Egy szerkeszthet?? c??ges ??rt??k lek??rdez??se", nickname = "getEditableCompanyVirtue", notes = "Egy szerkeszthet?? c??ges ??rt??k lek??rdez??se", response = CompanyVirtueEditableModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Egy szerkeszthet?? c??ges ??rt??k", response = CompanyVirtueEditableModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/{virtueId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue(@ApiParam(value = "Kiv??lasztott c??ges ??rt??k",required=true) @PathVariable("virtueId") Long virtueId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getEditableCompanyVirtue(virtueId);
        }
        return new ResponseEntity<CompanyVirtueEditableModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy szerkeszthet?? c??ges ??rt??k m??dos??t??sa", nickname = "modifyEditableCompanyVirtue", notes = "Egy szerkeszthet?? c??ges ??rt??k m??dos??t??sa Csak az editvalue lesz m??dos??tva", response = CompanyVirtueEditableModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "C??ges ??rt??kek lek??rdez??se", response = CompanyVirtueEditableModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/{virtueId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<CompanyVirtueEditableModel> modifyEditableCompanyVirtue(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CompanyVirtueEditableModel body,@ApiParam(value = "Kiv??lasztott c??ges ??rt??k",required=true) @PathVariable("virtueId") Long virtueId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_modifyEditableCompanyVirtue(body, virtueId);
        }
        return new ResponseEntity<CompanyVirtueEditableModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy szerkeszthet?? c??ges ??rt??k publik??l??sa", nickname = "publicEditableCompanyVirtue", notes = "Egy szerkeszthet?? c??ges ??rt??k publik??l??sa Ha van editvalue, akkor azt a value hely??re m??solja", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "C??ges ??rt??kek publik??l??sa"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/public/{virtueId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    public ResponseEntity<Void> publicEditableCompanyVirtue(@ApiParam(value = "Kiv??lasztott c??ges ??rt??k",required=true) @PathVariable("virtueId") Long virtueId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_publicEditableCompanyVirtue(virtueId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "C??ges ??rt??kek lek??rdez??se", nickname = "queryEditableCompanyVirtues", notes = "Szerkeszthet?? C??ges ??rt??kek lek??rdez??se", response = CompanyVirtueEditablePageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "virtue", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Szerkeszthet?? c??ges ??rt??kek lek??rdez??se", response = CompanyVirtueEditablePageModel.class),
        @ApiResponse(code = 204, message = "Nincs v??lasz"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
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



