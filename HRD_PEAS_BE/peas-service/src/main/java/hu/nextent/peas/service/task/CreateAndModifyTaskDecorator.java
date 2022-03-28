package hu.nextent.peas.service.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.entity.CompanyVirtue;
import hu.nextent.peas.jpa.entity.Difficulty;
import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.EvaluationStatusEnum;
import hu.nextent.peas.jpa.entity.Factor;
import hu.nextent.peas.jpa.entity.LeaderVirtue;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.TaskXCompanyVirtue;
import hu.nextent.peas.jpa.entity.TaskXFactor;
import hu.nextent.peas.jpa.entity.TaskXLeaderVirtue;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.CompanyVirtueModel;
import hu.nextent.peas.model.LeaderVirtueModel;
import hu.nextent.peas.model.TaskCreateModel;
import hu.nextent.peas.model.TaskEvaluationModel;
import hu.nextent.peas.model.TaskFactorModel;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.model.UserSimpleModel;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CreateAndModifyTaskDecorator extends AbstarctTaskDecorator {
	

    public ResponseEntity<TaskModel> createTask(TaskCreateModel body) {
    	log.debug("createTask");
    	log.debug(body.toString());
    	
    	validateAndPrepareCreateTask(body);
    	Task task = createTaskFromModel(body);

    	return getTaskModel(task);
    }
    
    void validateAndPrepareCreateTask(TaskCreateModel body) {
    	log.debug("validateAndPrepareCreateTask");
    	
    	if (body.getName() == null || body.getName().length() <= 0) {
    		ExceptionList.task_name_empty(null);
    	}
    	
    	if (body.getDifficulty() == null || body.getDifficulty().getId() == null) {
    		ExceptionList.task_difficulty_not_founded(null, null);
    	}

    	Optional<Difficulty> optDifficulty = difficultyRepository.findById(body.getDifficulty().getId());
    	if (!optDifficulty.isPresent() || optDifficulty.get().getAutomatic()) {
    		ExceptionList.task_difficulty_not_founded(null, body.getDifficulty().getId());
    	}
    	
    	if (!CollectionUtils.isEmpty(body.getTaskfactors())) {
    		for(TaskFactorModel factorModel: body.getTaskfactors()) {
    			if (factorModel.getId() == null) {
    				ExceptionList.task_factor_not_founded(null, null);
    			}
    			val optFactor = factorRepository.findById(factorModel.getId());
    			if (!optFactor.isPresent()) {
    				ExceptionList.task_factor_not_founded(null, factorModel.getId());
    			}
    			val factor = optFactor.get();
    			if (!getCurrentCompany().equals(factor.getCompany())) {
    				ExceptionList.task_factor_not_founded(null, factorModel.getId());
    			}
    			if (factor.getAutomatic()) {
    				ExceptionList.task_factor_not_founded(null, factorModel.getId());
    			}
    		}
    	}
    	
    	
    	// Fönök tulajdonosa az érték ?
    	if (!CollectionUtils.isEmpty(body.getLeaderVirtues())) {
    		User leaderUser = getCurrentLeader();
    		for(LeaderVirtueModel leaderVirtueModel: body.getLeaderVirtues()) {
    			if (leaderVirtueModel.getId() == null) {
    				throw ExceptionList.task_leadervirtue_not_founded(null, null);
    			}
    			Optional<LeaderVirtue> optLeaderVirtue = leaderVirtueRepository.findById(leaderVirtueModel.getId());
    			if(!optLeaderVirtue.isPresent()) {
    				throw ExceptionList.task_leadervirtue_not_founded(null, leaderVirtueModel.getId());
    			}
    			LeaderVirtue leaderVirtue = optLeaderVirtue.get();
    			if (!leaderVirtue.getOwner().equals(leaderUser)) {
    				throw ExceptionList.task_leadervirtue_not_founded(null, leaderVirtueModel.getId());
    			}
    		}
    	}
    	
    	if (!CollectionUtils.isEmpty(body.getCompanyVirtues())) {
    		for(CompanyVirtueModel companyVirtueModel : body.getCompanyVirtues()) {
    			if (companyVirtueModel.getId() == null) {
    				throw ExceptionList.task_companyvirtue_not_founded(null, null);
    			}
    			Optional<CompanyVirtue> optCompanyVirtue = companyVirtueRepository.findById(companyVirtueModel.getId());
    			if(!optCompanyVirtue.isPresent()) {
    				throw ExceptionList.task_companyvirtue_not_founded(null, companyVirtueModel.getId());
    			}
    		}
    	}
    }
    
    Task createTaskFromModel(TaskCreateModel body) {
    	log.debug("createTaskFromModel");
		Task task = new Task();
		// Default
		task.setStatus(TaskStatusEnum.PARAMETERIZATION);
		task.setTaskType(TaskTypeEnum.NORMAL);
		task.setOwner(getCurrentUser());
		task.setCompany(getCurrentCompany());
		// Basic
		task.setName(body.getName());
		task.setDescription(body.getDescription());
		task.setDeadline(body.getDeadline());
		task.setDifficulty(difficultyRepository.getOne(body.getDifficulty().getId()));
		taskRepository.save(task);
		
		// CompanyVirtues
		List<TaskXCompanyVirtue> companyVirtues = task.getTaskXCompanyVirtues();
				
		long cnt = body.getCompanyVirtues()
			.stream()
			.filter(p -> p !=null)
			.map(CompanyVirtueModel::getId)
			.filter(p -> p !=null)
			.map(companyVirtueRepository::getOne)
			.map(p -> new TaskXCompanyVirtue(task, p))
			.peek(taskXCompanyVirtueRepository::save)
			.peek(companyVirtues::add)
			.count();
		
		if (cnt > 0) {
			taskRepository.save(task);
			log.debug("add {} company virtues", cnt);
		} else {
			log.debug("add 0 company virtue", cnt);
		}
		
		// LeaderVirtues
		List<TaskXLeaderVirtue> leaderVirtues = task.getTaskXLeaderVirtues();
		
		cnt = body.getLeaderVirtues()
			.stream()
			.filter(p -> p !=null)
			.map(LeaderVirtueModel::getId)
			.filter(p -> p !=null)
			.map(leaderVirtueRepository::getOne)
			.map(p -> new TaskXLeaderVirtue(task, p))
			.peek(taskXLeaderVirtueRepository::save)
			.peek(leaderVirtues::add)
			.count();
		
		if (cnt > 0) {
			taskRepository.save(task);
			log.debug("add {} leader virtues", cnt);
		} else {
			log.debug("add 0 leader virtue", cnt);
		}
		
		// Factors
		List<TaskXFactor> factors = task.getTaskXFactors();
		
		cnt = body.getTaskfactors()
			.stream()
			.filter(p -> p != null)
			.map(p -> new TaskXFactor(
							task, 
							factorRepository.getOne(p.getId()), 
							Boolean.TRUE.equals(p.isRequired())
						)
					)
			.peek(taskXFactorRepository::save)
			.peek(factors::add)
			.count();
		
		if (cnt > 0) {
			taskRepository.save(task);
			log.debug("add {} factors", cnt);
		} else {
			log.debug("add 0 factor", cnt);
		}
		
		// Users
		List<User> users = new ArrayList<User>();
		// NormalUsers
		body.getEvaluators()
			.stream()
			.filter(p -> p != null)
			.map(TaskEvaluationModel::getEvaluator)
			.filter(p -> p != null)
			//.filter(p -> UserSimpleModel.ModeEnum.NORMAL.equals(p.getMode()))
			.map(UserSimpleModel::getId)
			.filter(p -> p != null)
			.distinct()
			.map(p -> userRepository.getOne(p))
			.forEach(users::add);
		;
		
		int evaulatorCnt = 0;
		for(User u: users) {
			Evaluation eval = Evaluation.builder()
					.status(EvaluationStatusEnum.BEFORE_EVALUATING)
					.company(getCurrentCompany())
					.evaluator(u)
					.task(task)
					.build();
			evaluationRepository.save(eval);
			task.getEvaluations().add(eval);
			evaulatorCnt++;
		}
		
		
		if (evaulatorCnt != 0) {
			taskRepository.save(task);
			log.debug("add {} evaulators", evaulatorCnt);
		} else {
			log.debug("add 0 evaulator");
		}
		
		// EvaluaterCount
		task.setEvaluaterCount(evaulatorCnt);
		taskRepository.save(task);
		return task;
    }
    
    public ResponseEntity<TaskModel> modifyEditableTask(TaskModel body, Long taskId) {
    	log.debug("modifyEditableTask, taskId: {}", taskId);
    	log.debug(body.toString());
    	
    	validateModifyEditableTask(body, taskId);
		Task task = replaceEditableTask(body, taskId);
		return getTaskModel(task);
    }
    
    
    
    
    void validateModifyEditableTask(TaskModel body, Long checkTaskId) {
    	log.debug("validateModifyEditableTask, taskId: {}", checkTaskId);
    	if (body.getId() == null || checkTaskId == null) {
    		throw ExceptionList.task_id_reqired();
    	}
    	checkExists(checkTaskId);
		if (checkTaskId != null && !checkTaskId.equals(body.getId())) {
    		throw ExceptionList.task_invalid_id(checkTaskId);
		}
		// Old Task
		Task task = taskRepository.getOne(body.getId());
		checkActive(task);
		checkCompany(task);
		checkStatus(task, TaskStatusEnum.PARAMETERIZATION);
		checkType(task, TaskTypeEnum.NORMAL, TaskTypeEnum.TEMPLATE);
		checkCurrentUserEdit(task);
    	// Replace Task

    	if (StringUtils.isEmpty(body.getStatus())) {
    		throw ExceptionList.task_invalid_status(checkTaskId, Arrays.asList(TaskStatusEnum.PARAMETERIZATION), null);
    	}
    	if (!TaskStatusEnum.PARAMETERIZATION.name().equals(body.getStatus())) {
    		throw ExceptionList.task_invalid_status(checkTaskId, Arrays.asList(TaskStatusEnum.PARAMETERIZATION), body.getStatus());
    	}
		if (body.getName() == null || body.getName().length() <= 0) {
			ExceptionList.task_name_empty(null);
		}
    	if (body.getDifficulty() == null || body.getDifficulty().getId() == null) {
    		throw ExceptionList.task_difficulty_not_founded(checkTaskId, null);
    	}
    	if (!difficultyRepository.findById(body.getDifficulty().getId()).isPresent()) {
    		throw ExceptionList.task_difficulty_not_founded(checkTaskId, body.getDifficulty().getId());
    	}

    	// Fönök tulajdonosa az érték ?
    	if (!CollectionUtils.isEmpty(body.getLeaderVirtues())) {
    		User leaderUser = getCurrentLeader();
    		for(LeaderVirtueModel leaderVirtueModel: body.getLeaderVirtues()) {
    			if (leaderVirtueModel.getId() == null) {
    				throw ExceptionList.task_leadervirtue_not_founded(checkTaskId, null);
    			}
    			Optional<LeaderVirtue> optLeaderVirtue = leaderVirtueRepository.findById(leaderVirtueModel.getId());
    			if(!optLeaderVirtue.isPresent()) {
    				throw ExceptionList.task_leadervirtue_not_founded(checkTaskId, leaderVirtueModel.getId());
    			}
    			LeaderVirtue leaderVirtue = optLeaderVirtue.get();
    			if (!leaderVirtue.getOwner().equals(leaderUser)) {
    				throw ExceptionList.task_leadervirtue_not_founded(checkTaskId, leaderVirtueModel.getId());
    			}
    		}
    	}
    	
    	if (!CollectionUtils.isEmpty(body.getCompanyVirtues())) {
    		for(CompanyVirtueModel companyVirtueModel : body.getCompanyVirtues()) {
    			if (companyVirtueModel.getId() == null) {
    				throw ExceptionList.task_companyvirtue_not_founded(checkTaskId, null);
    			}
    			Optional<CompanyVirtue> optCompanyVirtue = companyVirtueRepository.findById(companyVirtueModel.getId());
    			if(!optCompanyVirtue.isPresent()) {
    				throw ExceptionList.task_companyvirtue_not_founded(checkTaskId, companyVirtueModel.getId());
    			}
    		}
    	}
    	
    	if (!CollectionUtils.isEmpty(body.getTaskfactors())) {
    		for(TaskFactorModel factorModel: body.getTaskfactors()) {
    			if (factorModel.getId() == null) {
    				ExceptionList.task_factor_not_founded(checkTaskId, null);
    			}
    			val optFactor = factorRepository.findById(factorModel.getId());
    			if (!optFactor.isPresent()) {
    				ExceptionList.task_factor_not_founded(checkTaskId, factorModel.getId());
    			}
    			val factor = optFactor.get();
    			if (!getCurrentCompany().equals(factor.getCompany())) {
    				ExceptionList.task_factor_not_founded(checkTaskId, factorModel.getId());
    			}
    			if (factor.getAutomatic()) {
    				ExceptionList.task_factor_not_founded(checkTaskId, factorModel.getId());
    			}
    		}
    	}
    	
    }
    
    private Task replaceEditableTask(TaskModel body, Long checkTaskId) {
    	log.debug("replaceEditableTask");
    	Task task = prepareTask(body);
		refreshCompanyVirtues(task, body);
		refreshLeaderVirtues(task, body);
		refreshFactors(task, body);
		refreshNormalEvaluaters(task, body);
		//refreshVirtualUser(task, body);
		refreshEvaluaterCount(task);
		taskRepository.save(task);
		return task;
    }
    
    

    private Task prepareTask(TaskModel body) {
    	Task task = taskRepository.getOne(body.getId());
		task.setName(body.getName());
		task.setDescription(body.getDescription());
		task.setDeadline(body.getDeadline());
		task.setDifficulty(difficultyRepository.getOne(body.getDifficulty().getId()));
		taskRepository.save(task);
		taskRepository.flush();
		return task;
    }

    
    private void refreshCompanyVirtues(Task task, TaskModel body) {
    	log.debug("refreshCompanyVirtues, taskId: {}", task.getId());
    	log.debug("taskId: {}, old company virtues count {}", task.getId(), task.getTaskXCompanyVirtues().size());
    	
    	List<Long> companyVirtueIds = 
    			body.getCompanyVirtues() == null || body.getCompanyVirtues().isEmpty()
    			? Collections.emptyList() 
    			: body.getCompanyVirtues()
	    			.stream()
	    			.filter(p -> p != null)
	    			.map(CompanyVirtueModel::getId)
	    			.filter(p -> p != null)
	    			.collect(Collectors.toList());

    	// Delete Old
    	log.debug("deleteOldCompanyVirtues, taskId: {}", task.getId());
    	int cntDel = 0;
    	List<TaskXCompanyVirtue> olds = new ArrayList<>();
    	for(TaskXCompanyVirtue taskXCompanyVirtue : task.getTaskXCompanyVirtues()) {
    		val companyViruteId = taskXCompanyVirtue.getCompanyVirtue().getId();
    		if (!companyVirtueIds.contains(companyViruteId)) {
    			olds.add(taskXCompanyVirtue);
    		}
    	}
    	
    	if (!olds.isEmpty()) {
			task.getTaskXCompanyVirtues().removeAll(olds);
			olds.forEach(taskXCompanyVirtueRepository::delete);
			cntDel = olds.size();
    	}
    	log.debug("deleted {} company virtues, taskId: {}", cntDel, task.getId());
    	

    	// Add New
    	log.debug("addNewCompanyVirtues, taskId: {}", task.getId());
       	int cntAdd = 0;
       	for(Long newCompanyViruteId: companyVirtueIds) {
       		val exits = 
       				task.getTaskXCompanyVirtues()
				   	.stream()
				   	.map(TaskXCompanyVirtue::getCompanyVirtue)
				   	.map(CompanyVirtue::getId)
				   	.anyMatch(p -> p.equals(newCompanyViruteId));
       		if (!exits) {
       			CompanyVirtue addCompanyVirtue = companyVirtueRepository.getOne(newCompanyViruteId);
       			if (addCompanyVirtue.getActive()) {
       				TaskXCompanyVirtue addTaskCompanyVirtue = 
       						TaskXCompanyVirtue.builder()
       							.task(task)
       							.companyVirtue(addCompanyVirtue)
       							.build();
       				taskXCompanyVirtueRepository.save(addTaskCompanyVirtue);
        			task.getTaskXCompanyVirtues().add(addTaskCompanyVirtue);
       				cntAdd ++;
       			} else {
       				log.debug("company virtue: {}, not active, not addable", addCompanyVirtue.getId());
       			}
		   }
       	}
       	if (cntDel > 0 || cntAdd > 0) {
       		taskRepository.save(task);
       	}
    	log.debug("add {} company virtues, taskId: {}", cntAdd, task.getId());
    }
    
    private void refreshLeaderVirtues(Task task, TaskModel body) {
    	log.debug("refreshLeaderVirtues, taskId: {}", task.getId());
    	log.debug("taskId: {}, old leader virtues count {}", task.getId(), task.getTaskXLeaderVirtues().size());
    	
    	
    	
    	List<Long> leaderVirtueIds =
    			body.getLeaderVirtues() == null || body.getLeaderVirtues().isEmpty() 
    				? Collections.emptyList()
    				: body.getLeaderVirtues()
		    			.stream()
		    			.filter(p -> p != null)
		    			.map(LeaderVirtueModel::getId)
		    			.filter(p -> p != null)
		    			.collect(Collectors.toList());
    	
    	boolean changed = false;
    	// Delete
    	log.debug("deleteOldLeaderVirtues, taskId: {}", task.getId());
    	int cntDel = 0;
    	List<TaskXLeaderVirtue> olds = new ArrayList<>();
    	for(TaskXLeaderVirtue taskXLeaderVirtue : task.getTaskXLeaderVirtues()) {
    		val leaderViruteId = taskXLeaderVirtue.getLeaderVirtue().getId();
    		if (!leaderVirtueIds.contains(leaderViruteId)) {
    			olds.add(taskXLeaderVirtue);
    		}
    	}
    	if (!olds.isEmpty()) {
			task.getTaskXLeaderVirtues().removeAll(olds);
			olds.forEach(taskXLeaderVirtueRepository::delete);
			cntDel = olds.size();
			changed = true;
    	}
    	log.debug("del {} leader virtues, taskId: {}", cntDel, task.getId());
    	// Add
    	log.debug("addNewLeaderVirtues, taskId: {}", task.getId());
  	   	int cntAdd = 0;
  	   	for(Long newLeaderViruteId: leaderVirtueIds) {
  	   		val exits = 
  	   				task.getTaskXLeaderVirtues()
  				   	.stream()
  				   	.map(TaskXLeaderVirtue::getLeaderVirtue)
  				   	.map(LeaderVirtue::getId)
  				   	.anyMatch(p -> p.equals(newLeaderViruteId));
  	   		if (!exits) {
  	   			val leaderVirtue = leaderVirtueRepository.getOne(newLeaderViruteId);
  	   			if (leaderVirtue.getActive()) {
  	   				val taskLeaderVirtues = TaskXLeaderVirtue.builder().task(task).leaderVirtue(leaderVirtue).build();
  	   				taskXLeaderVirtueRepository.save(taskLeaderVirtues);
  	   				task.getTaskXLeaderVirtues().add(taskLeaderVirtues);
  	   				cntAdd ++;
  	   				changed = true; 
  	   			} else {
  	   				log.debug("leader virtue: {}, not active, not addable", leaderVirtue.getId());
  	   			}
  	   		}
  	   	}
  	   	if (changed) {
  	   		taskRepository.save(task);
  	   	}
  	   	log.debug("add {} leader virtues, taskId: {}", cntAdd, task.getId());
    }
    

   
    private void refreshFactors(Task task, TaskModel body) {
    	log.debug("refreshFactors, taskId: {}", task.getId());
    	log.debug("taskId: {}, factors count {}", task.getId(), task.getTaskXFactors().size());
    	
    	Map<Long, Boolean> taskFactors =
    			body.getTaskfactors() == null || body.getTaskfactors().isEmpty() 
    			? Collections.emptyMap()
    			: body.getTaskfactors()
	    			.stream()
	    			.filter(p -> p != null)
	    			.filter(p -> p.getId() != null)
	    			.collect(Collectors.toMap(p -> p.getId(), p -> Boolean.TRUE.equals(p.isRequired())));
    	
    	boolean changed = false;
    	// Update
    	log.debug("update, taskId: {}", task.getId());
    	int cntUpdate = 0; 
    	for(Long editedFactorId : taskFactors.keySet()) {
    		val optTaskFactor = 
 				   task.getTaskXFactors()
 				   	.stream()
 				   	.filter(p -> p.getFactor().getId().equals(editedFactorId))
 				   	.findFirst();
    		
    		if(optTaskFactor.isPresent() && optTaskFactor.get().getRequired() != taskFactors.getOrDefault(editedFactorId, null)) {
    			val taskFactor = optTaskFactor.get();
    			taskFactor.setRequired(taskFactors.getOrDefault(editedFactorId, false));
    			taskXFactorRepository.save(taskFactor);
    			cntUpdate++;
    			changed = true;
    		}
    	}
    	log.debug("updated {} factor, taskId: {}", cntUpdate, task.getId());
    	// Delete
    	log.debug("deleteOldFactors, taskId: {}", task.getId());
    	int cntDel = 0;
    	List<TaskXFactor> olds = new ArrayList<>();
    	for(TaskXFactor taskXFactor : task.getTaskXFactors()) {
    		val factorId = taskXFactor.getFactor().getId();
    		if (!taskFactors.containsKey(factorId)) {
    			olds.add(taskXFactor);
    		}
    	}
    	if (!olds.isEmpty()) {
			task.getTaskXFactors().removeAll(olds);
			olds.forEach(taskXFactorRepository::delete);
			cntDel = olds.size();
			changed = true;
    	}
    	log.debug("delete {} factor, taskId: {}", cntDel, task.getId());
    	// Add
    	log.debug("deleteOldFactors, taskId: {}", task.getId());
    	int cntAdd = 0;
    	for(Long editedFactorId : taskFactors.keySet()) {
    		boolean exist = 
  				   task.getTaskXFactors()
  				   	.stream()
  				   	.anyMatch(p -> p.getFactor().getId().equals(editedFactorId))
  				   	;
    		if (!exist) {
    			Factor factor = factorRepository.getOne(editedFactorId);
    			if (factor.getActive()) {
	    			TaskXFactor taskXFactor = 
	    					TaskXFactor.builder()
	    						.task(task)
	    						.factor(factor)
	    						.required(taskFactors.getOrDefault(editedFactorId, false))
	    						.build();
	    			taskXFactorRepository.save(taskXFactor);
	    			task.getTaskXFactors().add(taskXFactor);
	    			cntAdd ++;
	    			changed = true;
    			} else {
 				   log.debug("factor: {}, not active, not addable", factor.getId());
    			}
    		}
    	}
    	
    	if (changed) {
    		taskRepository.save(task);
    	}
    	log.debug("add {} factor, taskId: {}", cntAdd, task.getId());
    }
    
   
    private void refreshNormalEvaluaters(Task task, TaskModel body) {
    	if (TaskTypeEnum.TEMPLATE.name().equals(body.getTaskType())) {
    		return;
    	}
    	List<Long> normalUserIds = 
    			body.getEvaluators() == null || body.getEvaluators().isEmpty()
    			? Collections.emptyList()
    			: body.getEvaluators().stream()
	    			.filter(p -> p != null)
	    			.map(TaskEvaluationModel::getEvaluator)
	    			.filter(p -> p != null)
	    			.map(UserSimpleModel::getId)
	    			.collect(Collectors.toList())
	    			;
    	
    	boolean changed = false;
    	int cntDel = 0;
    	List<Evaluation> olds = new ArrayList<>();
    	for(Evaluation evaluation : task.getEvaluations()) {
    		val user = evaluation.getEvaluator();
    		val userId = user.getId();
    		if (!normalUserIds.contains(userId)) {
    			olds.add(evaluation);
    		}
    	}
    	if (!olds.isEmpty()) {
			task.getEvaluations().removeAll(olds);
			olds.forEach(evaluationRepository::delete);
			cntDel = olds.size();
			changed = true;
    	}
    	log.debug("del {} user, taskId: {}", cntDel, task.getId());

    	int cntAdd = 0;
    	for(Long userId: normalUserIds) {
    		boolean exist = 
   				   task.getEvaluations()
   				   	.stream()
   				   	.anyMatch(p -> p.getEvaluator().getId().equals(userId))
   				   	;
    		if (!exist) {
    			val user = userRepository.getOne(userId);
    			if (user.getActive()) {
	    			val evaluation =
	    					Evaluation.builder()
	    						.task(task)
	    						.company(getCurrentCompany())
	    						.status(EvaluationStatusEnum.BEFORE_EVALUATING)
	    						.evaluator(user)
	    						.build();
	    			evaluationRepository.save(evaluation);
	    			task.getEvaluations().add(evaluation);
	    			cntAdd ++;
	    			changed = true;
    			} else {
    				throw ExceptionList.task_evaluator_not_founded(task.getId(), userId);
    			}
    		}
    	}
    	
    	if (changed) {
    		task.setEvaluaterCount(task.getEvaluations().size());
    		taskRepository.save(task);
    	}
    	log.debug("add {} user, taskId: {}", cntAdd, task.getId());
    }
    
}
