package hu.nextent.peas.jpa.dao;

import java.util.List;
import java.util.Optional;

import hu.nextent.peas.jpa.entity.Company;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.User;

public interface TaskRepository extends DaoRepository<Task, Long> {

	
	List<Task> findAllByOwnerAndPeriod(User owner, Period period);
	
	Optional<Task> findByOwnerAndPeriodAndTaskType(User owner, Period period, TaskTypeEnum taskType);
	
	List<Task> findAllByOwnerAndStatusOrderById(User owner, TaskStatusEnum status);
	
	List<Task> findAllByCompanyAndTaskTypeAndStatus(Company company, TaskTypeEnum taskType, TaskStatusEnum status);

	int countByPeriodAndTaskTypeAndStatus(Period period, TaskTypeEnum taskType, TaskStatusEnum status);

}
