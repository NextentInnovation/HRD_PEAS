import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';
import { DateRange } from '@src/app/models/task/task-list-search.model';
import { BaseModel } from '@src/app/models/base/base.model';

export class NotificationSearchModel extends BaseSearch {
    createdDateRange: DateRange;
    hideReaded: boolean;
    markReaded: boolean;

    constructor() {
        super();
        this.createdDateRange = new DateRange();
    }
}

export class NotificationModel extends BaseModel {
    body: string;
    createdDate: Date;
    id: number;
    notificationType: string;
    reference: number;
    referenceType: string;
    status: string;
    subject: string;
    readed: boolean;
}
