/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.11).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package hu.nextent.peas.rating.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.RatingSendModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Api(value = "rating_v1", description = "the rating_v1 API")
public interface RatingV1Api {

    Logger log = LoggerFactory.getLogger(RatingV1Api.class);

    default Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }
	

    @ApiOperation(value = "Vezetői minősítés küldése", nickname = "saveRating", notes = "Vezetői minősítés adatainak küldése.      Ellenőrzés:   - Létező Rating objektum ratingId alapján.   - A belépett felhasználó a vezető benne.   - Még nem minősített.   - Kötelező adatok (textualEvaluation, gradeRecommendation, cooperation) megléte.    Működés:   - Rating tárolás.   - Rating állapot állítás.   - Teendő állapot állítás: kész.   - Értesítés állapot állítás: kész.", tags={ "rating", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/rating_v1/{ratingId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    default ResponseEntity<Void> saveRating(@ApiParam(value = "" ,required=true )  @Valid @RequestBody RatingSendModel body,@ApiParam(value = "Kiválasztott vezetői minősítés",required=true) @PathVariable("ratingId") Long ratingId) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default RatingV1Api interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
