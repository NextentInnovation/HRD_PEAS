package hu.nextent.peas.period.api;

import hu.nextent.peas.model.CreatePeriodModel;
import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.PeriodModel;
import hu.nextent.peas.model.PeriodPageModel;
import hu.nextent.peas.model.PeriodQueryParameterModel;
import hu.nextent.peas.service.PeriodService;

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

@Controller("hu.nextent.peas.period.api.PeriodV1Api")
@Api(value = "period_v1", description = "the period_v1 API")
public class PeriodV1ApiController implements PeriodV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public PeriodV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "??j peri??dus l??trehoz??sa", nickname = "createPeriod", notes = "??j peri??dus adatainak ment??se.      Ellen??rz??s:   - Csak ??zleti admin szerepk??r f??rhet hozz?? (BUSINESS_ADMIN).    Megadott adatok jav??t??sa:   - Ha nincs megadva valamely adat, gener??lni kell.   - Ha nincs megadva a v??ge d??tum, a min??s??t??s v??ge d??tum is gener??l??sra ker??l.   - V??ge d??tuma az el??z?? peri??dus min??s??t??s v??ge d??tum ut??ni. Ha nem ??gy van, a d??tumok gener??l??dnak.   - Min??s??t??s v??ge d??tuma a v??ge d??tum ut??ni. Ha nem ??gy van, gener??l??dik.   - A n??vnek egyedinek kell lennie. Ha nem ??gy van, ??gy gener??l??dik, hogy egyedi legyen.    M??k??d??s:   - Adatok alapj??n ??j peri??dus l??trehoz??sa.", response = PeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "??j peri??dus l??trehoz??sa", response = PeriodModel.class),
        @ApiResponse(code = 204, message = "Nincs v??lasz"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    public ResponseEntity<PeriodModel> createPeriod(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CreatePeriodModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_createPeriod(body);
        }
        return new ResponseEntity<PeriodModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Peri??dus t??rl??se", nickname = "deletePeriod", notes = "Peri??dus t??rl??se.      Ellen??rz??s:   - Csak ??zleti admin szerepk??r f??rhet hozz?? (BUSINESS_ADMIN).   - Csak a legutols?? j??v??beli peri??dus t??r??lhet??.  M??velet:   - Peri??dus t??rl??se.", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Sikeres t??rl??s"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1/{periodId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePeriod(@ApiParam(value = "Kiv??lasztott peri??dus azonos??t??ja",required=true) @PathVariable("periodId") Long periodId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_deletePeriod(periodId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy peri??dus lek??rdez??se", nickname = "getPeriod", notes = "Egy peri??dus lek??rdez??se azonos??t?? alapj??n.      Ellen??rz??s:   - Csak ??zleti admin szerepk??r f??rhet hozz?? (BUSINESS_ADMIN).", response = PeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Peri??dus lek??rdez??s", response = PeriodModel.class),
        @ApiResponse(code = 204, message = "Nincs v??lasz"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1/{periodId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<PeriodModel> getPeriod(@ApiParam(value = "Kiv??lasztott peri??dus azonos??t??ja",required=true) @PathVariable("periodId") Long periodId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getPeriod(periodId);
        }
        return new ResponseEntity<PeriodModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy peri??dus m??dos??t??sa", nickname = "modifyPeriod", notes = "Peri??dus adatainak ment??se.      Ellen??rz??s:   - Csak ??zleti admin szerepk??r f??rhet hozz?? (BUSINESS_ADMIN).    Megadott adatok jav??t??sa:   - Csak a peri??dus ??llapot??nak megfelel?? adatok v??ltozhatnak:     - J??v??beli: name, v??ge, min??s??t??s v??ge     - Aktu??lis: n??v, v??ge (csak j??v??beli lehet), min??s??t??s v??ge     - Min??s??t??s alatt: n??v, min??s??t??s v??ge (csak j??v??beli lehet)     - Lez??rt: n??v   - Ha nincs megadva valamely adat, gener??lni kell.   - Ha nincs megadva a v??ge d??tum, a min??s??t??s v??ge d??tum is gener??l??sra ker??l.   - V??ge d??tuma az el??z?? peri??dus min??s??t??s v??ge d??tum ut??ni. Ha nem ??gy van, a d??tumok gener??l??dnak.   - Min??s??t??s v??ge d??tuma a v??ge d??tum ut??ni. Ha nem ??gy van, gener??l??dik.   - A n??vnek egyedinek kell lennie. Ha nem ??gy van, ??gy gener??l??dik, hogy egyedi legyen.    M??k??d??s:   - Adatok alapj??n peri??dus ment??se.   - Ha v??ltozott a min??s??t??s v??ge d??tum, a k??vetkez?? peri??dusok v??ge d??tumai sorra ellen??rzend??ek ??s m??dos??tand??ak, ha kor??bbi.", response = PeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Peri??dus lek??rdez??s", response = PeriodModel.class),
        @ApiResponse(code = 204, message = "Nincs v??lasz"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1/{periodId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<PeriodModel> modifyPeriod(@ApiParam(value = "" ,required=true )  @Valid @RequestBody PeriodModel body,@ApiParam(value = "Kiv??lasztott peri??dus azonos??t??ja",required=true) @PathVariable("periodId") Long periodId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_modifyPeriod(body, periodId);
        }
        return new ResponseEntity<PeriodModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "K??vetekez?? peri??dus minta lek??rdez??se", nickname = "periodTemplate", notes = "A peri??dus l??trehoz??s??hoz az utols?? peri??dus ut??nit gener??l?? minta lek??rdez??se.", response = CreatePeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Minta peri??dus", response = CreatePeriodModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1/next",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<CreatePeriodModel> periodTemplate()
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_periodTemplate();
        }
        return new ResponseEntity<CreatePeriodModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Peri??dus lista lek??rdez??se", nickname = "queryPeriod", notes = "A peri??dusok adatainak visszaad??sa sz??rhet?? ??s rendezhet?? m??don.      Ellen??rz??s:   - Csak ??zleti admin szerepk??r f??rhet hozz?? (BUSINESS_ADMIN).", response = PeriodPageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Peri??dus lista lek??rdez??se", response = PeriodPageModel.class),
        @ApiResponse(code = 204, message = "Nincs v??lasz"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<PeriodPageModel> queryPeriod(@ApiParam(value = "" ,required=true )  @Valid @RequestBody PeriodQueryParameterModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_queryPeriod(body);
        }
        return new ResponseEntity<PeriodPageModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	@Autowired private PeriodService periodService;
	
    public ResponseEntity<PeriodModel> inner_createPeriod( CreatePeriodModel  body) {
        return periodService.createPeriod(body);
    }
    public ResponseEntity<Void> inner_deletePeriod( Long  periodId) {
    	return periodService.deletePeriod(periodId);
    }
    public ResponseEntity<PeriodModel> inner_getPeriod( Long  periodId) {
        return periodService.getPeriod(periodId);
    }
    public ResponseEntity<PeriodModel> inner_modifyPeriod( PeriodModel  body, Long  periodId) {
    	return periodService.modifyPeriod(body, periodId);
    }
    public ResponseEntity<CreatePeriodModel> inner_periodTemplate() {
        return periodService.periodTemplate();
    }
    public ResponseEntity<PeriodPageModel> inner_queryPeriod( PeriodQueryParameterModel  body) {
        return periodService.queryPeriod(body);
    }


   // ----------------------------------------------------------------------------------------

   

}



