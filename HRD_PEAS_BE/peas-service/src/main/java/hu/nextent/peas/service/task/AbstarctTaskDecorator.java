package hu.nextent.peas.service.task;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.facades.FactoryServiceNotification;
import hu.nextent.peas.jpa.dao.CompanyVirtueRepository;
import hu.nextent.peas.jpa.dao.DatabaseInfoRepository;
import hu.nextent.peas.jpa.dao.EvaluationRepository;
import hu.nextent.peas.jpa.dao.FactorRepository;
import hu.nextent.peas.jpa.dao.LeaderVirtueRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.dao.TaskXCompanyVirtueRepository;
import hu.nextent.peas.jpa.dao.TaskXFactorRepository;
import hu.nextent.peas.jpa.dao.TaskXLeaderVirtueRepository;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.dao.ViewTaskRepository;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.model.TaskModel;
import hu.nextent.peas.service.base.BaseDecorator;
import hu.nextent.peas.utils.UserServiceHelper;
import lombok.val;

public abstract class AbstarctTaskDecorator 
extends BaseDecorator
{
	
	@Autowired protected TaskRepository taskRepository;
	@Autowired protected ViewTaskRepository viewTaskRepository;
	@Autowired protected DatabaseInfoRepository databaseInfoRepository;
	@Autowired protected CompanyVirtueRepository companyVirtueRepository;
	@Autowired protected TaskXCompanyVirtueRepository taskXCompanyVirtueRepository;
	@Autowired protected LeaderVirtueRepository leaderVirtueRepository;
	@Autowired protected TaskXLeaderVirtueRepository taskXLeaderVirtueRepository;
	@Autowired protected TaskXFactorRepository taskXFactorRepository;
	@Autowired protected FactorRepository factorRepository;
	@Autowired protected EvaluationRepository evaluationRepository;
	@Autowired protected PeriodRepository periodRepository;
	@Autowired protected ToDoRepository toDoRepository;
	@Autowired protected NotificationRepository notificationRepository;
	@Autowired protected FactoryServiceNotification factoryServiceNotification;
	@Autowired protected UserServiceHelper userServiceHelper;
	
	protected void checkExists(Long taskId) {
    	taskRepository.findById(taskId).orElseThrow(() -> ExceptionList.task_not_founded(taskId));
    }
    
	protected void checkActive(Task task) {
    	if (TaskStatusEnum.DELETED.equals(task.getStatus())) {
    		throw ExceptionList.task_invalid_status(task.getId(), Arrays.asList(TaskStatusEnum.DELETED), task.getStatus());
    	}
    }
    
	protected void checkCompany(Task task) {
    	if (!task.getCompany().equals(getCurrentCompany())) {
    		throw ExceptionList.task_invalid_company(task.getId(), task.getCompany().getId());
    	}
    }
	
	protected void checkStatus(Task task, TaskStatusEnum ... validStatuses) {
    	if (!Arrays.asList(validStatuses).contains(task.getStatus())) {
    		throw ExceptionList.task_invalid_status(task.getId(), Arrays.asList(validStatuses), task.getStatus());
    	}
    }
    
	protected void checkType(Task task, TaskTypeEnum ... validTypes) {
    	if (!Arrays.asList(validTypes).contains(task.getTaskType())) {
    		throw ExceptionList.task_invalid_type(task.getId(), Arrays.asList(validTypes), task.getTaskType());
    	}
    }

    protected void checkCurrentUserEdit(Task task) {
		if (!getCurrentUser().equals(task.getOwner())) {
			throw ExceptionList.task_invalid_user(task.getId(), task.getOwner().getId());
		}
	}
	
    protected void checkCurrentUserView(Task task) {
		User user = getCurrentUser();
		List<RoleEnum> roleEnumList = getCurrentUserRoleEnum();
		if (!roleEnumList.contains(RoleEnum.HR)) {
			if (roleEnumList.contains(RoleEnum.LEADER)) {
				if (!user.equals(task.getOwner()) && !task.getOwner().isLedBy(user)) {
					throw ExceptionList.task_invalid_user(task.getId(), task.getOwner().getId());
				}
			} else if (roleEnumList.contains(RoleEnum.USER)) {
				if (!user.equals(task.getOwner())) {
					throw ExceptionList.task_invalid_user(task.getId(), task.getOwner().getId());
				}
			} else {
				throw ExceptionList.task_invalid_user(task.getId(), task.getOwner().getId());
			}
		}
	}

	protected ResponseEntity<TaskModel> getTaskModel(Long taskId) {
    	checkExists(taskId);
    	val task = taskRepository.getOne(taskId);
    	return getTaskModel(task);
    }
	
	protected ResponseEntity<TaskModel> getTaskModel(Task task) {
    	return ResponseEntity.ok(conversionService.convert(task, TaskModel.class));
    }
	
	
	protected Range<Integer> getFactorRange() {
		return Range.between(companyServiceHelper.getTaskFactorMin(), companyServiceHelper.getTaskFactorMax());
	}

	protected Integer getFactorRequiredMin() {
		return companyServiceHelper.getTaskFactorReqMin();
	}

	protected Range<Integer> getCompanyVirtueRange() {
		return Range.between(companyServiceHelper.getTaskCompanyVirtueMin(), companyServiceHelper.getTaskCompanyVirtueMax());
	}

	protected Range<Integer> getLeaderVirtueRange() {
		return Range.between(companyServiceHelper.getTaskLeaderVirtueMin(), companyServiceHelper.getTaskLeaderVirtueMax());
	}

	protected Range<Integer> getUserRange() {
		return Range.between(companyServiceHelper.getTaskUserMin(), companyServiceHelper.getTaskUserMax());
	}
	
	public Integer getEvaluationExiredDay() {
		return companyServiceHelper.getEvaluationExpiredDay();
	}
	
	protected void refreshEvaluaterCount(Task task) {
    	if (TaskTypeEnum.TEMPLATE.equals(task.getTaskType())) {
    		task.setEvaluaterCount(0);
    	} else {
    		int userCount = task.getEvaluations().size();
    		task.setEvaluaterCount(userCount);
    	}
    	taskRepository.save(task);
    	taskRepository.flush();
    }

	

}
