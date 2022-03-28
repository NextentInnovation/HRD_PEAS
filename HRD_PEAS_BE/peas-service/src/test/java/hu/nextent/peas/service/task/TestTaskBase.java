package hu.nextent.peas.service.task;

import org.springframework.beans.factory.annotation.Autowired;

import hu.nextent.peas.jpa.dao.CompanyVirtueRepository;
import hu.nextent.peas.jpa.dao.DifficultyRepository;
import hu.nextent.peas.jpa.dao.FactorRepository;
import hu.nextent.peas.jpa.dao.LeaderVirtueRepository;
import hu.nextent.peas.jpa.dao.NotificationRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.dao.ToDoRepository;
import hu.nextent.peas.jpa.dao.UserRepository;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.service.TaskService;
import hu.nextent.peas.service.TestServiceBase;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
public abstract class TestTaskBase extends TestServiceBase {
	
	@Autowired
	protected TaskRepository taskRepository;
	
	@Autowired
	protected ToDoRepository toDoRepository;
	
	@Autowired
	protected NotificationRepository notificationRepository;
	
	@Autowired
	protected CompanyVirtueRepository companyVirtueRepository;
	
	@Autowired
	protected LeaderVirtueRepository leaderVirtueRepository;
	
	@Autowired
	protected DifficultyRepository difficultyRepository;

	@Autowired
	protected FactorRepository factorRepository;
	
	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected TaskService taskService;
	
	private Task selectedTask;
	
	

	protected void givenFirstTaskByStatus(TaskStatusEnum taskStatus) {
		val tasks = taskRepository.findAllByOwnerAndStatusOrderById(getSelectedUser(), taskStatus);
		selectedTask = tasks.get(0);
	}
	
	protected void givenFirstTaskByParameterization() {
		givenFirstTaskByStatus(TaskStatusEnum.PARAMETERIZATION);
	}

}
