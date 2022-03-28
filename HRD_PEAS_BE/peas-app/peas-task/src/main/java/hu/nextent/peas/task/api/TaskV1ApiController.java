package hu.nextent.peas.task.api;

import hu.nextent.peas.model.ErrorMessageModel;
import hu.nextent.peas.model.TaskCreateModel;
import java.time.OffsetDateTime;
import hu.nextent.peas.model.StringListModel;
import hu.nextent.peas.model.TaskItemPageModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.TaskQueryParameterModel;
import hu.nextent.peas.service.TaskService;

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

@Controller("hu.nextent.peas.task.api.TaskV1Api")
@Api(value = "task_v1", description = "the task_v1 API")
public class TaskV1ApiController implements TaskV1Api {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TaskV1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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



    @ApiOperation(value = "Szerkeszthető task másolása", nickname = "copyTask", notes = "Szerkeszthető task másolása Az következő taskok másolhatóak:   - Saját taskjai   - Vezető taskjai A másolás során az értékelők nem kerülnek másolásra. A task NORMAL típusként jön létre", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Másolt task", response = TaskModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/copy_v1/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    public ResponseEntity<TaskModel> copyTask(@ApiParam(value = "Kiválasztott szerkeszthető task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_copyTask(taskId);
        }
        return new ResponseEntity<TaskModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Új szerkeszthető task létrehozása", nickname = "createTask", notes = "Új szerkeszthető task létrehozása", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Paraméterezhető task", response = TaskModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    public ResponseEntity<TaskModel> createTask(@ApiParam(value = "" ,required=true )  @Valid @RequestBody TaskCreateModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_createTask(body);
        }
        return new ResponseEntity<TaskModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Szerkeszthető task törlése", nickname = "deleteTask", notes = "Szerkeszthető task törlése Törölhető, amelyik:     - a felhasználó saját tulajdonában van      - Aktív     - szerkeszthető státuszú", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Sikeres törlés"),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTask(@ApiParam(value = "Kiválasztott task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_deleteTask(taskId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Adott referenciával rendelkező task lekérdezése", nickname = "getTask", notes = "Adott referenciával rendelkező task lekérdezése Minden olyan task lekérdhető, amely:   * Aktív   * Role esetén:     * ADMIN: Minegyik     * HR: Mindegyik     * USER: Saját     * LEADER: Saját és beosztottak", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Taskok lekérdezás", response = TaskModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<TaskModel> getTask(@ApiParam(value = "Kiválasztott task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getTask(taskId);
        }
        return new ResponseEntity<TaskModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Szerkeszthető task módosítása", nickname = "modifyTask", notes = "Szerkeszthető task módosítása Amelyik    - a felhasználó saját tulajdonában van    - Aktív   - szerkeszthető státuszú", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Lekérdezett szerkeszthető task", response = TaskModel.class),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/{taskId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<TaskModel> modifyTask(@ApiParam(value = "" ,required=true )  @Valid @RequestBody TaskModel body,@ApiParam(value = "Kiválasztott task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_modifyTask(body, taskId);
        }
        return new ResponseEntity<TaskModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Taskok lekérdezése", nickname = "queryTask", notes = "Taskok lekérdezése Itt csak saját és beosztottak taskjai kérdezhetőek le", response = TaskItemPageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Taskok lekérdezése", response = TaskItemPageModel.class),
        @ApiResponse(code = 204, message = "Nincs válasz"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<TaskItemPageModel> queryTask(@ApiParam(value = "" ,required=true )  @Valid @RequestBody TaskQueryParameterModel body)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_queryTask(body);
        }
        return new ResponseEntity<TaskItemPageModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Értékelés indítása", nickname = "startEvaluation", notes = "Értékelés indítása Csak olyan taskok szerkeszthetőek, amelyeknél igaz, hogy: - rendelkezik értékelőkkel - típusa: NORMAL/AUTOMATIC - státusza: PARAMETERIZATION", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Sikeres indítás"),
        @ApiResponse(code = 200, message = "Általános hibaüzenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/start_evaluation/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    public ResponseEntity<Void> startEvaluation(@ApiParam(value = "Kiválasztott szerkeszthető task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_startEvaluation(taskId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

	@javax.annotation.PostConstruct
	public void registerModule() {
		// Register module with class
		hu.nextent.peas.module.ModuleSigleton.getInstance().registerModule(this.getClass());
	}
	
    // ----------------------------------------------------------------------------------------
	
    @Autowired private TaskService taskService;
	
    public ResponseEntity<TaskModel> inner_copyTask( Long  taskId) {
        return taskService.copyTask(taskId);
    }
    public ResponseEntity<TaskModel> inner_createTask( TaskCreateModel  body) {
        return taskService.createTask(body);
    }
    public ResponseEntity<Void> inner_deleteTask( Long  taskId) {
        return taskService.deleteTask(taskId);
    }
    public ResponseEntity<TaskModel> inner_getTask( Long  taskId) {
        return taskService.getTask(taskId);
    }
    public ResponseEntity<TaskModel> inner_modifyTask( TaskModel  body, Long  taskId) {
        return taskService.modifyTask(body, taskId);
    }
    public ResponseEntity<TaskItemPageModel> inner_queryTask( TaskQueryParameterModel  body) {
        return taskService.queryTask(body);
    }
    public ResponseEntity<Void> inner_startEvaluation( Long  taskId) {
        return taskService.startEvaluation(taskId);
    }


   // ----------------------------------------------------------------------------------------

   
}



