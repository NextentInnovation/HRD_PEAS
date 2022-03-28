package hu.nextent.peas.service.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.dao.PeriodRepository;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.entity.Period;
import hu.nextent.peas.jpa.entity.Task;
import hu.nextent.peas.jpa.entity.TaskStatusEnum;
import hu.nextent.peas.jpa.entity.TaskTypeEnum;
import hu.nextent.peas.jpa.entity.User;
import hu.nextent.peas.service.base.BaseDecorator;


public abstract class AbstractReportDecorator extends BaseDecorator {

    @PersistenceContext
    EntityManager entityManager;

	public static final String REPORT_EMPLOYEE = "report_employee";  
	public static final String REPORT_ALL_EMPLOYEE = "report_all_employee";  
	public static final String REPORT_EVALUATION = "report_evaluation";  
	
	public static final List<TaskStatusEnum> REPORT_TASK_STATUS = new ArrayList<TaskStatusEnum>(); 
	static {
		REPORT_TASK_STATUS.add(TaskStatusEnum.UNDER_EVALUATION);
		REPORT_TASK_STATUS.add(TaskStatusEnum.EVALUATED);
		REPORT_TASK_STATUS.add(TaskStatusEnum.CLOSED);
	};

    @Autowired
    protected PeriodRepository periodRepository;

    @Autowired
    protected RatingRepository ratingRepository;

    @Autowired
    protected TaskRepository taskRepository;

    protected Period getPeriod(String reportCode, Long periodId) {
    	if (periodId == null) {
    		throw ExceptionList.report_period_not_founded(reportCode, periodId);
    	}
        Period period = periodRepository.findById(periodId).orElseThrow(() -> ExceptionList.report_period_not_founded(reportCode, periodId));
        if (!period.getCompany().equals(getCurrentCompany())) {
        	throw ExceptionList.report_period_not_founded(reportCode, periodId);
        }
        return period;
    }

    protected User getUser(String reportCode, Long userId) {
    	if (userId == null) {
    		throw ExceptionList.report_user_not_founded(reportCode, userId);
    	}
        User user = userRepository.findById(userId).orElseThrow(() -> ExceptionList.report_user_not_founded(reportCode, userId));
        if (!user.getCompany().equals(getCurrentCompany())) {
        	throw ExceptionList.report_user_not_founded(reportCode, userId);
        }
        return user;
    }

    protected Task getTask(String reportCode, Long taskId) {
    	if (taskId == null) {
    		throw ExceptionList.report_task_not_founded(reportCode, taskId);
    	}
        Task task = taskRepository.findById(taskId).orElseThrow(() -> ExceptionList.report_user_not_founded(reportCode, taskId));
        if (!task.getCompany().equals(getCurrentCompany())) {
        	throw ExceptionList.report_task_not_founded(reportCode, taskId);
        }
        if (task.getPeriod() == null) {
        	throw ExceptionList.report_task_not_founded(reportCode, taskId);
        }
        if (!TaskTypeEnum.NORMAL.equals(task.getTaskType())) {
            throw ExceptionList.report_task_not_founded(reportCode, taskId);
        }
        if (!REPORT_TASK_STATUS.contains(task.getStatus())) {
            throw ExceptionList.report_task_not_founded(reportCode, taskId);
        }
        return task;
    }

    protected static Double bigDecimalToDouble(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : bigDecimal.doubleValue();
    }

    protected String queryAsLeaderOrganization(User leader) {
        if (leader == null) {
            return null;
        }
        return (String) queryOneValue(
                " select organization from user u where u.leader_id = :leaderId limit 1 "
                , "leaderId", leader.getId()
        );
    }

    protected Double queryAsLeaderOrganizationScore(User leader, Period period) {
        if (leader == null || period == null) {
            return null;
        }
        return bigDecimalToDouble(queryOneValue(
                " select avg(r2.periodScore) from rating as r2 " +
                " inner join user as u9 on u9.id = r2.user_id " +
                " where r2.periodScore is not null " +
                " and r2.periodScore > 0 " +
                " and r2.period_id = :periodId " +
                " and 0 < (select count(*) from user as u3 where u3.leader_id = :leaderId) " +
                " and u9.organizationPath like :organizationPathLike "
                , "organizationPathLike", leader.getOrganizationPath() + "_%"
                , "periodId", period.getId()
                , "leaderId", leader.getId()
        ));
    }

    protected Object queryOneValue(String sql, Object... oddNameEvenValue) {
        // csak erős idegzetűeknek
        Query query = entityManager.createNativeQuery(sql);
        for(int i = 0, l = oddNameEvenValue.length; i < l; i += 2) {
            if (oddNameEvenValue[i + 1] == null) return null;
            query.setParameter((String) oddNameEvenValue[i], oddNameEvenValue[i + 1]);
        }
        @SuppressWarnings("unchecked")
		List<Object> list = query.getResultList();
        if (list.size() > 0 && list.get(0) != null) {
            return list.get(0);
        }
        return null;
    }

    protected static Long bigIntegerToLong(Object o) {
        return
                o == null ? null :
                        ((BigInteger) o).longValue();
    }

    protected static Double bigDecimalToDouble(Object o) {
        return
                o == null ? null :
                        ((BigDecimal) o).doubleValue();
    }

    protected static boolean noData(String o) {
        return o == null || o.length() <= 0;
    }

    protected static boolean noData(Long o) {
        return o == null;
    }


}
