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



    @ApiOperation(value = "Új periódus létrehozása", nickname = "createPeriod", notes = "Új periódus adatainak mentése.      Ellenőrzés:   - Csak üzleti admin szerepkör férhet hozzá (BUSINESS_ADMIN).    Megadott adatok javítása:   - Ha nincs megadva valamely adat, generálni kell.   - Ha nincs megadva a vége dátum, a minősítés vége dátum is generálásra kerül.   - Vége dátuma az előző periódus minősítés vége dátum utáni. Ha nem így van, a dátumok generálódnak.   - Minősítés vége dátuma a vége dátum utáni. Ha nem így van, generálódik.   - A névnek egyedinek kell lennie. Ha nem így van, úgy generálódik, hogy egyedi legyen.    Működés:   - Adatok alapján új periódus létrehozása.", response = PeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Új periódus létrehozása", response = PeriodModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
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
    

    @ApiOperation(value = "Periódus törlése", nickname = "deletePeriod", notes = "Periódus törlése.      Ellenőrzés:   - Csak üzleti admin szerepkör férhet hozzá (BUSINESS_ADMIN).   - Csak a legutolsó jövőbeli periódus törölhető.  Művelet:   - Periódus törlése.", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Sikeres törlés"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1/{periodId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePeriod(@ApiParam(value = "Kiválasztott periódus azonosítója",required=true) @PathVariable("periodId") Long periodId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_deletePeriod(periodId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy periódus lekérdezése", nickname = "getPeriod", notes = "Egy periódus lekérdezése azonosító alapján.      Ellenőrzés:   - Csak üzleti admin szerepkör férhet hozzá (BUSINESS_ADMIN).", response = PeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Periódus lekérdezás", response = PeriodModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1/{periodId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<PeriodModel> getPeriod(@ApiParam(value = "Kiválasztott periódus azonosítója",required=true) @PathVariable("periodId") Long periodId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getPeriod(periodId);
        }
        return new ResponseEntity<PeriodModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Egy periódus módosítása", nickname = "modifyPeriod", notes = "Periódus adatainak mentése.      Ellenőrzés:   - Csak üzleti admin szerepkör férhet hozzá (BUSINESS_ADMIN).    Megadott adatok javítása:   - Csak a periódus állapotának megfelelő adatok változhatnak:     - Jövőbeli: name, vége, minősítés vége     - Aktuális: név, vége (csak jövőbeli lehet), minősítés vége     - Minősítés alatt: név, minősítés vége (csak jövőbeli lehet)     - Lezárt: név   - Ha nincs megadva valamely adat, generálni kell.   - Ha nincs megadva a vége dátum, a minősítés vége dátum is generálásra kerül.   - Vége dátuma az előző periódus minősítés vége dátum utáni. Ha nem így van, a dátumok generálódnak.   - Minősítés vége dátuma a vége dátum utáni. Ha nem így van, generálódik.   - A névnek egyedinek kell lennie. Ha nem így van, úgy generálódik, hogy egyedi legyen.    Működés:   - Adatok alapján periódus mentése.   - Ha változott a minősítés vége dátum, a következő periódusok vége dátumai sorra ellenőrzendőek és módosítandóak, ha korábbi.", response = PeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Periódus lekérdezás", response = PeriodModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/period_v1/{periodId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<PeriodModel> modifyPeriod(@ApiParam(value = "" ,required=true )  @Valid @RequestBody PeriodModel body,@ApiParam(value = "Kiválasztott periódus azonosítója",required=true) @PathVariable("periodId") Long periodId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_modifyPeriod(body, periodId);
        }
        return new ResponseEntity<PeriodModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Követekező periódus minta lekérdezése", nickname = "periodTemplate", notes = "A periódus létrehozásához az utolsó periódus utánit generáló minta lekérdezése.", response = CreatePeriodModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Minta periódus", response = CreatePeriodModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
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
    

    @ApiOperation(value = "Periódus lista lekérdezése", nickname = "queryPeriod", notes = "A periódusok adatainak visszaadása szűrhető és rendezhető módon.      Ellenőrzés:   - Csak üzleti admin szerepkör férhet hozzá (BUSINESS_ADMIN).", response = PeriodPageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "period", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Periódus lista lekérdezése", response = PeriodPageModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
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



