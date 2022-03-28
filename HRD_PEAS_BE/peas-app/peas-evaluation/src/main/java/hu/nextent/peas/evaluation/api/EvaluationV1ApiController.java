package hu.nextent.peas.evaluation.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.EvaluationModel;
import hu.nextent.peas.service.EvaluationService;

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

@Controller("hu.nextent.peas.evaluation.api.EvaluationV1Api")
@Api(value = "evaluation_v1", description = "the evaluation_v1 API")
public class EvaluationV1ApiController implements EvaluationV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public EvaluationV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Egy értékeléshez adatok lekérdezése", nickname = "getEvaluation", notes = "A feladat adatainak lekérése az értékelés elvégzéséhez.      Ellenőrzés:   - Létező értékelés.   - A belépett felhasználó az értékelés értékelője, és még nem értékelte.", response = EvaluationModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "evaluation", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Egy értékeléshez adatok lekérdezése", response = EvaluationModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/evaluation_v1/{evaluationId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<EvaluationModel> getEvaluation(@ApiParam(value = "Értékelés azonosítója",required=true) @PathVariable("evaluationId") Long evaluationId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getEvaluation(evaluationId);
        }
        return new ResponseEntity<EvaluationModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Értékelés küldése", nickname = "modifyAndSendEvaluation", notes = "Egy értékelés küldése.      Ellenőrzés:   - Létező értékelés.   - A belépett felhasználó az értékelés értékelője, és még nem értékelte.   - Kötelező értékelési szempontokhoz van-e választás.    Működés:   - A feladat értékelési szempontjainak választási lehetőségeiből választás, valamint szöveges értékelés tárolása.   - Értékelés pontszám számítása és tárolása.   - Egy értékelés alatt lévő értékelést értékelt állapotba állítja: UNDER_EVALUATION -> EVALUATED   - Hozzá tartozó tennivaló lezárása.   - Hozzá tartozó értesítés kész-re állítása.   - A belépett felhasználóhoz tartozó automatikus feladathoz egy IGEN értékelés készítése.   - Ha ő az utolsó értékelő: Feladat értékeltre állítása folyamat.", response = EvaluationModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "evaluation", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Értékelés küldése", response = EvaluationModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/evaluation_v1/{evaluationId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<EvaluationModel> modifyAndSendEvaluation(@ApiParam(value = "" ,required=true )  @Valid @RequestBody EvaluationModel body,@ApiParam(value = "Értékelés azonosítója",required=true) @PathVariable("evaluationId") Long evaluationId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_modifyAndSendEvaluation(body, evaluationId);
        }
        return new ResponseEntity<EvaluationModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
    @Autowired private EvaluationService evaluationService;
	
    public ResponseEntity<EvaluationModel> inner_getEvaluation( Long  evaluationId) {
        return evaluationService.getEvaluation(evaluationId);
    }
    public ResponseEntity<EvaluationModel> inner_modifyAndSendEvaluation( EvaluationModel  body, Long  evaluationId) {
        return evaluationService.modifyAndSendEvaluation(body, evaluationId);
    }


   // ----------------------------------------------------------------------------------------

   

}



