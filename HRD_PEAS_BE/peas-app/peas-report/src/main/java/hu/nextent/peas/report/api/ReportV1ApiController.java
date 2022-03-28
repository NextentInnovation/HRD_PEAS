package hu.nextent.peas.report.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.ReportAllEmployeePageModel;
import hu.nextent.peas.model.ReportAllEmployeeQueryParameterModel;
import hu.nextent.peas.model.ReportEmployee;
import hu.nextent.peas.model.ReportEmployeeQueryModel;
import hu.nextent.peas.model.ReportEvaluationModel;
import hu.nextent.peas.model.ReportPeriodListModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import hu.nextent.peas.service.ReportService;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller("hu.nextent.peas.report.api.ReportV1Api")
@Api(value = "report_v1", description = "the report_v1 API")
public class ReportV1ApiController implements ReportV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ReportV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Aktív, minősítés alatt és lezárt periódusok lekérdezése", nickname = "queryPeriod", notes = "Periódusok lekérdezése endDate szerint csökkenő sorrendben.", response = ReportPeriodListModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "report", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Periódusok lekérdezése", response = ReportPeriodListModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/report_v1/period_v1",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<ReportPeriodListModel> queryPeriod()
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_queryPeriod();
        }
        return new ResponseEntity<ReportPeriodListModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Összes dolgozó riport", nickname = "reportAllEmployee", notes = "Adott periódusban a belépett felhasználó által elérhető felhasználók és vezetők pontszámai és csoportpontjai.      Ellenőrzés:   - Csak aktív, minősítés alatt és lezárt periódus lehet.    Működés:   - Riport visszaadása.", response = ReportAllEmployeePageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "report", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Összes dolgozó riport", response = ReportAllEmployeePageModel.class),
        @ApiResponse(code = 404, message = "Általános hibaüzenet", response = ErrorMessageModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/report_v1/employees_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<ReportAllEmployeePageModel> reportAllEmployee(@ApiParam(value = "" ,required=true )  @Valid @RequestBody ReportAllEmployeeQueryParameterModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_reportAllEmployee(body);
        }
        return new ResponseEntity<ReportAllEmployeePageModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Dolgozó riport", nickname = "reportEmployee", notes = "Adott periódus adott felhasználó részletei és feladatainak riportja.      Paraméterezése:   - vagy userId és periodId   - vagy ratingId  Ellenőrzés:   - Csak aktív, minősítés alatt és lezárt periódus lehet.   - Csak a belépett felhasználó által látható felhasználó lehet.     - HR szerepkör minden felhasználót láthat.     - Vezető szerepkör saját magát és az általa vezetett csoport összes felhasználóját alcsoportokkal együtt.     - User szerepkör csak saját magát.  Működés:   - Felhasználó, vezetője, csoportja, pont, vezetőként csoportja, csoport átlag   - Céges átlag   - Vezetői periódus minősítés adatai.   - Feladatok adatai.", response = ReportEmployee.class, tags={ "report", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Dolgozói riport", response = ReportEmployee.class),
        @ApiResponse(code = 404, message = "Általános hibaüzenet", response = ErrorMessageModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/report_v1/employee_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<ReportEmployee> reportEmployee(@ApiParam(value = "" ,required=true )  @Valid @RequestBody ReportEmployeeQueryModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_reportEmployee(body);
        }
        return new ResponseEntity<ReportEmployee>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Feladat értékelés riport", nickname = "reportEvaluation", notes = "Adott feladat részleteinek és értékeléseinek a riportja.      Ellenőrzés:   - Csak a belépett felhasználó által látható feladat lehet.     - HR szerepkör minden feladatot láthat.     - Vezető szerepkör saját és az általa vezetett csoport összes feladatot alcsoportokkal együtt.     - User szerepkör csak saját feladatot.    Működés:   - A felhasználó saját feladata anonimizálva kerül visszaadásra (az értékelők neve nélkül).   - Felhasználó, vezetője, csoportja, pont, vezetőként csoportja, csoport átlag   - Céges átlag   - Vezetői periódus minősítés adatai.   - Feladat adatai.   - A feladat értékelési szempontjai.   - A feladat értékelői által adott válaszok pontszámai értékelési szempontonként.   - Szöveges értékelések.   - Összátlagok.", response = ReportEvaluationModel.class, tags={ "report", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Feladat értékelés riport", response = ReportEvaluationModel.class),
        @ApiResponse(code = 404, message = "Általános hibaüzenet", response = ErrorMessageModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/report_v1/evaluation_v1/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    public ResponseEntity<ReportEvaluationModel> reportEvaluation(@ApiParam(value = "Task azonosító",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_reportEvaluation(taskId);
        }
        return new ResponseEntity<ReportEvaluationModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	@Autowired
    ReportService reportService;

    public ResponseEntity<ReportPeriodListModel> inner_queryPeriod() {
        return reportService.queryPeriod();
    }
    public ResponseEntity<ReportAllEmployeePageModel> inner_reportAllEmployee( ReportAllEmployeeQueryParameterModel  body) {
        return reportService.reportAllEmployee(body);
    }
    public ResponseEntity<ReportEmployee> inner_reportEmployee( ReportEmployeeQueryModel  body) {
        return reportService.reportEmployee(body);
    }
    public ResponseEntity<ReportEvaluationModel> inner_reportEvaluation( Long  taskId) {
        return reportService.reportEvaluation(taskId);
    }


   // ----------------------------------------------------------------------------------------

   

}



