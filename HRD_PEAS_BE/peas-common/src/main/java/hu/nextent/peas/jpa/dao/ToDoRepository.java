package hu.nextent.peas.jpa.dao;

import java.util.List;

import hu.nextent.peas.jpa.entity.Evaluation;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.ToDo;
import hu.nextent.peas.jpa.entity.ToDoTypeEnum;
import hu.nextent.peas.jpa.entity.User;

public interface ToDoRepository extends DaoRepository<ToDo, Long> {
	
	List<ToDo> findAllByToUserAndEvaluationAndToDoType(User toUser, Evaluation evaluation, ToDoTypeEnum toDoType);
	List<ToDo> findAllByRatingAndToDoType(Rating rating, ToDoTypeEnum toDoType);
	
	Long countByToUserAndToDoType(User toUser, ToDoTypeEnum toDoType);
}
