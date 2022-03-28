package hu.nextent.peas.resource.api;

import hu.nextent.peas.model.AutocompletModel;
import hu.nextent.peas.model.AutocompletQueryModel;
import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.service.PeriodService;
import hu.nextent.peas.service.ResourceService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller("hu.nextent.peas.resource.api.AutocompletV1Api")
@Api(value = "autocomplet_v1", description = "the autocomplet_v1 API")
public class AutocompletV1ApiController implements AutocompletV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AutocompletV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Task nevek gyorskereső", nickname = "queryAutocomplet", notes = "Taskok lekérdezése<br/> Keres a névben és a leírásban<br/> <br/> autocompletType: Lekérdezés típusa<br/>     Típusok:       - task<br/>         Task nevében Keres<br/>         ADMIN/HR: esetben minden taskban<br/>         USER: Csak saját taskban<br/>         LEADER: Beosztottak taskjaiban<br/>       - user<br/>         Minden felhasználóban keres<br/>       - company<br/>         ADMIN: Minden cég nevében<br/>         Egyébb esetben: Csak saját céget adja vissza<br/>       - difficulty<br/>         Az adott céghez tartozó nehézségekben<br/>       - companyVirtue<br/>         Az adott céghez céges értékekben<br/>       - leaderVirtue<br/>         A belépett felhasználó vezetőjének az csoport értékeiben<br/>       - factor<br/>         Az adott céghez rendelt szempontokban keres<br/>", response = AutocompletModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "resource", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Gyorskereső", response = AutocompletModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/autocomplet_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<AutocompletModel> queryAutocomplet(@ApiParam(value = "" ,required=true )  @Valid @RequestBody AutocompletQueryModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_queryAutocomplet(body);
        }
        return new ResponseEntity<AutocompletModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	@Autowired
	ResourceService resourceService;
	
    public ResponseEntity<AutocompletModel> inner_queryAutocomplet( AutocompletQueryModel  body) {
        return resourceService.queryAutocomplet(body);
    }


   // ----------------------------------------------------------------------------------------

   
}



