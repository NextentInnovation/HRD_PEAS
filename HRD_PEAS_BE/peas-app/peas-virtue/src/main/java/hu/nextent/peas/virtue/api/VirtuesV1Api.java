/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.11).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package hu.nextent.peas.virtue.api;

import hu.nextent.peas.model.CompanyVirtueEditableModel;
import hu.nextent.peas.model.CompanyVirtueEditablePageModel;
import hu.nextent.peas.model.CompanyVirtueQueryParameterModel;
import hu.nextent.peas.model.ErrorMessageModel;
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
@Api(value = "virtues_v1", description = "the virtues_v1 API")
public interface VirtuesV1Api {

    Logger log = LoggerFactory.getLogger(VirtuesV1Api.class);

    default Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
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
    default ResponseEntity<CompanyVirtueEditableModel> createCompanyVirtue(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CompanyVirtueEditableModel body) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", CompanyVirtueEditableModel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default VirtuesV1Api interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Egy szerkeszthető céges érték lekérdezése", nickname = "getEditableCompanyVirtue", notes = "Egy szerkeszthető céges érték lekérdezése", response = CompanyVirtueEditableModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Egy szerkeszthető céges érték", response = CompanyVirtueEditableModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/{virtueId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    default ResponseEntity<CompanyVirtueEditableModel> getEditableCompanyVirtue(@ApiParam(value = "Kiválasztott céges érték",required=true) @PathVariable("virtueId") Long virtueId) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", CompanyVirtueEditableModel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default VirtuesV1Api interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
    default ResponseEntity<CompanyVirtueEditableModel> modifyEditableCompanyVirtue(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CompanyVirtueEditableModel body,@ApiParam(value = "Kiválasztott céges érték",required=true) @PathVariable("virtueId") Long virtueId) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", CompanyVirtueEditableModel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default VirtuesV1Api interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @ApiOperation(value = "Egy szerkeszthető céges érték publikálása", nickname = "publicEditableCompanyVirtue", notes = "Egy szerkeszthető céges érték publikálása Ha van editvalue, akkor azt a value helyére másolja", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Céges értékek publikálása"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/virtues_v1/company_v1/public/{virtueId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    default ResponseEntity<Void> publicEditableCompanyVirtue(@ApiParam(value = "Kiválasztott céges érték",required=true) @PathVariable("virtueId") Long virtueId) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default VirtuesV1Api interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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
    default ResponseEntity<CompanyVirtueEditablePageModel> queryEditableCompanyVirtues(@ApiParam(value = "" ,required=true )  @Valid @RequestBody CompanyVirtueQueryParameterModel body) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", CompanyVirtueEditablePageModel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default VirtuesV1Api interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}