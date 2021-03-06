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



    @ApiOperation(value = "Szerkeszthet?? task m??sol??sa", nickname = "copyTask", notes = "Szerkeszthet?? task m??sol??sa Az k??vetkez?? taskok m??solhat??ak:   - Saj??t taskjai   - Vezet?? taskjai A m??sol??s sor??n az ??rt??kel??k nem ker??lnek m??sol??sra. A task NORMAL t??pusk??nt j??n l??tre", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "M??solt task", response = TaskModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/copy_v1/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    public ResponseEntity<TaskModel> copyTask(@ApiParam(value = "Kiv??lasztott szerkeszthet?? task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_copyTask(taskId);
        }
        return new ResponseEntity<TaskModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "??j szerkeszthet?? task l??trehoz??sa", nickname = "createTask", notes = "??j szerkeszthet?? task l??trehoz??sa", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Param??terezhet?? task", response = TaskModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
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
    

    @ApiOperation(value = "Szerkeszthet?? task t??rl??se", nickname = "deleteTask", notes = "Szerkeszthet?? task t??rl??se T??r??lhet??, amelyik:     - a felhaszn??l?? saj??t tulajdon??ban van      - Akt??v     - szerkeszthet?? st??tusz??", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Sikeres t??rl??s"),
        @ApiResponse(code = 204, message = "Nincs v??lasz"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTask(@ApiParam(value = "Kiv??lasztott task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent()) {
            return inner_deleteTask(taskId);
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Adott referenci??val rendelkez?? task lek??rdez??se", nickname = "getTask", notes = "Adott referenci??val rendelkez?? task lek??rdez??se Minden olyan task lek??rdhet??, amely:   * Akt??v   * Role eset??n:     * ADMIN: Minegyik     * HR: Mindegyik     * USER: Saj??t     * LEADER: Saj??t ??s beosztottak", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Taskok lek??rdez??s", response = TaskModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    public ResponseEntity<TaskModel> getTask(@ApiParam(value = "Kiv??lasztott task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_getTask(taskId);
        }
        return new ResponseEntity<TaskModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Szerkeszthet?? task m??dos??t??sa", nickname = "modifyTask", notes = "Szerkeszthet?? task m??dos??t??sa Amelyik    - a felhaszn??l?? saj??t tulajdon??ban van    - Akt??v   - szerkeszthet?? st??tusz??", response = TaskModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Lek??rdezett szerkeszthet?? task", response = TaskModel.class),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/{taskId}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    public ResponseEntity<TaskModel> modifyTask(@ApiParam(value = "" ,required=true )  @Valid @RequestBody TaskModel body,@ApiParam(value = "Kiv??lasztott task",required=true) @PathVariable("taskId") Long taskId)
	{
        if (getAcceptHeader().isPresent() && getAcceptHeader().get().contains("application/json")) {
            return inner_modifyTask(body, taskId);
        }
        return new ResponseEntity<TaskModel>(HttpStatus.NOT_IMPLEMENTED);
    }
    

    @ApiOperation(value = "Taskok lek??rdez??se", nickname = "queryTask", notes = "Taskok lek??rdez??se Itt csak saj??t ??s beosztottak taskjai k??rdezhet??ek le", response = TaskItemPageModel.class, authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Taskok lek??rdez??se", response = TaskItemPageModel.class),
        @ApiResponse(code = 204, message = "Nincs v??lasz"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
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
    

    @ApiOperation(value = "??rt??kel??s ind??t??sa", nickname = "startEvaluation", notes = "??rt??kel??s ind??t??sa Csak olyan taskok szerkeszthet??ek, amelyekn??l igaz, hogy: - rendelkezik ??rt??kel??kkel - t??pusa: NORMAL/AUTOMATIC - st??tusza: PARAMETERIZATION", authorizations = {
        @Authorization(value = "bearerAuth")    }, tags={ "task", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Sikeres ind??t??s"),
        @ApiResponse(code = 200, message = "??ltal??nos hiba??zenet", response = ErrorMessageModel.class) })
    @RequestMapping(value = "/task_v1/start_evaluation/{taskId}",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    public ResponseEntity<Void> startEvaluation(@ApiParam(value = "Kiv??lasztott szerkeszthet?? task",required=true) @PathVariable("taskId") Long taskId)
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



