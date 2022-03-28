package hu.nextent.peas.todo.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.NotificationPageModel;
import hu.nextent.peas.model.NotificationQueryParameterModel;
import hu.nextent.peas.model.ToDoQueryParameterModel;
import hu.nextent.peas.service.ToDoAndNotificationService;

import java.time.OffsetDateTime;
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

@Controller("hu.nextent.peas.todo.api.NotificationV1Api")
@Api(value = "notification_v1", description = "the notification_v1 API")
public class NotificationV1ApiController implements NotificationV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public NotificationV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Értesítések lekérdezése", nickname = "queryNotification", notes = "Értesítések lekérdezése<br/> Saját felhasználónak feladott értesítések elemek lekérdezése<br/>", response = NotificationPageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "notification", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Értesítések lekérdezése", response = NotificationPageModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/notification_v1/",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<NotificationPageModel> queryNotification(@ApiParam(value = "" ,required=true )  @Valid @RequestBody NotificationQueryParameterModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_queryNotification(body);
        }
        return new ResponseEntity<NotificationPageModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	@Autowired
	ToDoAndNotificationService toDoAndNotificationService;
	
    public ResponseEntity<NotificationPageModel> inner_queryNotification( NotificationQueryParameterModel  body) {
        return toDoAndNotificationService.queryNotification(body);
    }


   // ----------------------------------------------------------------------------------------

   

}



