import { DateRange } from '@src/app/models/task/task-list-search.model';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';

export class PeriodSearch extends BaseSearch {
  endDateRange: DateRange;
  id: number;
  name: string;
  startDateRange: DateRange;
  ratingEndDateRange: DateRange;
  status: string[];

  constructor() {
    super();
    this.endDateRange = new DateRange();
    this.startDateRange = new DateRange();
    this.ratingEndDateRange = new DateRange();
  }
}

export interface Period {
  endDate: Date;
  id: number;
  name: string;
  ratingEndDate: Date;
  startDate: Date;
  status: string;
}
