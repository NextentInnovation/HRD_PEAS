--liquibase formatted sqlcreate view view_task asSELECT 	concat(t.id, '-', l_type.language) id,    l_type.language,    t.id taskId,    t.version,    t.created_by,    t.created_at,    t.modified_by,    t.modified_at,    t.deadline,    t.description,    t.endDate,    t.evaluatedCount,    t.evaluaterCount,    t.evaluationPercentage,    t.name,    t.score,    t.startDate,    t.status,    l_status.label statusName,    t.taskType,    l_type.label taskTypeName,    t.company_id,    t.difficulty_id,    t.owner_id,    t.period_idFROM    task t        LEFT JOIN label l_type ON (CONCAT('type.TaskType.', t.taskType) = l_type.code)        LEFT JOIN label l_status ON (CONCAT('status.TaskStatus.', t.status) = l_status.code)WHERE l_type.language = l_status.language;create view view_todo asSELECT	concat(t.id, '-', l_status.language) id,	l_status.language,    l_status.label statusName,    t.id todoId,    t.version,    t.created_by,    t.created_at,    t.modified_by,    t.modified_at,    t.deadline,    t.done,    t.messageCode,    t.referenceType,    t.status,    t.toDoType,    t.company_id,    t.evaluation_id,    t.leadervirtue_id,    t.period_id,    t.rating_id,    t.task_id,    t.to_user_idFROM    todo t        LEFT JOIN label l_status ON (CONCAT('constants.to_do_status.', LOWER(t.status)) = l_status.code);