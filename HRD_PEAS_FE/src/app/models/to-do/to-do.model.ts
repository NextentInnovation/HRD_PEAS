import { BaseModel } from '@src/app/models/base/base.model';
import { DateRange } from '@src/app/models/task/task-list-search.model';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';

export class ToDoSearchModel extends BaseSearch {
    deadlineRange: DateRange;
    doneRange: DateRange;
    id: number;
    status: string[];
    todoType: string[];

    constructor() {
        super();
        this.doneRange = new DateRange();
        this.deadlineRange = new DateRange();
    }
}

export class ToDoModel extends BaseModel {
    createdDate: Date;
    deadline: Date;
    deadlineComming: boolean;
    id: number;
    message: string;
    reference: number;
    referenceType: string;
    status: string;
    todoType: string;
    done: Date;
}
