package hu.nextent.peas.jpa.dao;


import hu.nextent.peas.jpa.entity.*;
import hu.nextent.peas.jpa.view.ViewToDo;

import java.util.List;

public interface ViewToDoRepository extends DaoRepository<ViewToDo, String> {
    List<ViewToDo> findAllByToUserAndEvaluationAndToDoType(User toUser, Evaluation evaluation, ToDoTypeEnum toDoType);
    List<ViewToDo> findAllByRatingAndToDoType(Rating rating, ToDoTypeEnum toDoType);

    Long countByToUserAndToDoType(User toUser, ToDoTypeEnum toDoType);
}
