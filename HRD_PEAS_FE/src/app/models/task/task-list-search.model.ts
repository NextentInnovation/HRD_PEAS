import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';

export class TaskListSearchModel extends BaseSearch {
    createdDateRange: DateRange;
    deadlineRange: DateRange;
    endDateRange: DateRange;
    description: string;
    id: number;
    name: string;
    ownerId: number;
    ownerName: string;
    percentageRange: NumberRange;
    scoreRange: NumberRange;
    startDateRange: DateRange;
    status: Array<string>;
    taskType: Array<string>;

    constructor() {
        super();
        this.createdDateRange = new DateRange();
        this.deadlineRange = new DateRange();
        this.startDateRange = new DateRange();
        this.endDateRange = new DateRange();
        this.percentageRange = new NumberRange();
        this.scoreRange = new NumberRange();
    }
}

export class DateRange {
    max: Date;
    min: Date;
}

export class NumberRange {
    max: number;
    min: number;
}
