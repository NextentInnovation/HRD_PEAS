package hu.nextent.peas.converters;

import hu.nextent.peas.cache.ServiceCaches;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.view.ViewToDo;
import hu.nextent.peas.model.ReferenceTypeEnumModel;
import hu.nextent.peas.model.ToDoModel;
import hu.nextent.peas.utils.CompanyServiceHelper;
import hu.nextent.peas.utils.SpelFormater;
import hu.nextent.peas.utils.UserServiceHelper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class TodoModelFromViewTodoConverter extends AbstractConverter<ViewToDo, ToDoModel>
{
	
	@Autowired
	private CompanyServiceHelper companyServiceHelper;
	
	@Autowired
	private UserServiceHelper userServiceHelper;
	
	@Autowired
	private ServiceCaches serviceCaches;
	
	@Override
	public ToDoModel convert(ViewToDo todo) {
		
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		val warningDate = now.minusDays(getDeadlineWarningDay(todo));
		
		val model = new ToDoModel();
		model
			.id(todo.getTodoId())
			.status(ToDoModel.StatusEnum.valueOf(todo.getStatus().name()))
			.todoType(todo.getToDoType().name())
			.deadlineComming(warningDate.compareTo(todo.getDeadline()) > 1)
			.done(todo.getDone())
			.createdDate(todo.getCreatedDate())
			.deadline(todo.getDeadline())
			.referenceType(ReferenceTypeEnumModel.valueOf(todo.getReferenceType().name()))
		;
		
		switch (todo.getReferenceType()) {
			case TASK:
				model.setReference(todo.getTask().getId());
				break;
			case EVALUATION:
				model.setReference(todo.getEvaluation().getId());
				break;
			case RATING:
				model.setReference(todo.getRating().getId());
				break;
			case LEADERVIRTUE:
				model.setReference(todo.getLeaderVirtue().getId());
				break;
			case PERIOD:
				model.setReference(todo.getPeriod().getId());
				break;
			default:
				break;
		}
		
		model.message(format(todo.getMessageCode(), todo));
		
		return model;
	}
	
	private Integer getDeadlineWarningDay(ViewToDo todo) {
		val company = todo.getCompany();
		val days = companyServiceHelper.getDeadlineTaskDays(company.getId());
		return days;
	}

	
	private String format(String template, ViewToDo todo) {
		String lang = userServiceHelper.getLanguage();
		String message = serviceCaches.getLabel(template, lang);
		String formated = SpelFormater.format(
				message, 
				todo, 
				serviceCaches.labelMapProxy(lang)
				);
		return formated;
	}

}
