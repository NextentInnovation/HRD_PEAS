/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.11).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package hu.nextent.peas.todo.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.ToDoPageModel;
import hu.nextent.peas.model.ToDoQueryParameterModel;
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
@Api(value = "todo_v1", description = "the todo_v1 API")
public interface TodoV1Api {

    Logger log = LoggerFactory.getLogger(TodoV1Api.class);

    default Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    default Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    default Optional<String> getAcceptHeader() {
        return getRequest().map(r -> r.getHeader("Accept"));
    }
	

    @ApiOperation(value = "Tennivaló lekérdezése", nickname = "queryTodo", notes = "Tennivaló lekérdezése<br/> Saját felhasználónak feladott TODO elemek lekérdezése<br/>", response = ToDoPageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "todo", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Teendő lekérdezése", response = ToDoPageModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/todo_v1/",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    default ResponseEntity<ToDoPageModel> queryTodo(@ApiParam(value = "" ,required=true )  @Valid @RequestBody ToDoQueryParameterModel body) {
        if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("\"\"", ToDoPageModel.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default TodoV1Api interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
