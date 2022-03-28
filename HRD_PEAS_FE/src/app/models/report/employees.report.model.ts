import { NumberRange } from '@src/app/models/task/task-list-search.model';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';
import { UserSimpleModel } from '@src/app/models/user/user.data.model';
import { PaginationListResponse } from '@src/app/models/base/pagination-list-response.interface';

export class EmployeesPaginationReportModel extends PaginationListResponse<EmployeesReportModel> {
    companyScoreAvg: number;
    organizationScoreAvg: number;
}

export class EmployeesReportModel {
    asLeaderOrganization: string;
    asLeaderOrganizationScore: number;
    employee: UserSimpleModel;
    leader: UserSimpleModel;
    score: number;
}

export class EmployeesReportSearchModel extends BaseSearch {
    leaderId: number;
    asLeaderOrganization: string;
    organization: string;
    asLeaderOrganizationScore: NumberRange;
    periodId: number;
    score: NumberRange;
    userId: number;

    constructor() {
        super();
        this.asLeaderOrganizationScore = new NumberRange();
        this.score = new NumberRange();
    }
}
