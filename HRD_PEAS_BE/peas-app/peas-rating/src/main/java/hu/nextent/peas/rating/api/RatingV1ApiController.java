package hu.nextent.peas.rating.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.RatingSendModel;
import hu.nextent.peas.service.RatingService;

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

@Controller("hu.nextent.peas.rating.api.RatingV1Api")
@Api(value = "rating_v1", description = "the rating_v1 API")
public class RatingV1ApiController implements RatingV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public RatingV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Vezetői minősítés küldése", nickname = "saveRating", notes = "Vezetői minősítés adatainak küldése.      Ellenőrzés:   - Létező Rating objektum ratingId alapján.   - A belépett felhasználó a vezető benne.   - Még nem minősített.   - Kötelező adatok (textualEvaluation, gradeRecommendation, cooperation) megléte.    Működés:   - Rating tárolás.   - Rating állapot állítás.   - Teendő állapot állítás: kész.   - Értesítés állapot állítás: kész.", tags={ "rating", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/rating_v1/{ratingId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    public ResponseEntity<Void> saveRating(@ApiParam(value = "" ,required=true )  @Valid @RequestBody RatingSendModel body,@ApiParam(value = "Kiválasztott vezetői minősítés",required=true) @PathVariable("ratingId") Long ratingId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_saveRating(body, ratingId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
	@Autowired
	private RatingService ratingService;
	
    public ResponseEntity<Void> inner_saveRating( RatingSendModel  body, Long  ratingId) {
        return ratingService.saveRating(body, ratingId);
    }


   // ----------------------------------------------------------------------------------------

   

}



