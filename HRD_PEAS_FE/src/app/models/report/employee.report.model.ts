import { UserSimpleModel } from '@src/app/models/user/user.data.model';
import { TaskItemModel } from '@src/app/models/task/task-item.model';
import { SimplePeriodModel } from '@src/app/models/base/base-datas.model';

export class EmployeeReportModel {
    period: SimplePeriodModel;
    asLeaderOrganization: string;
    asLeaderOrganizationScore: number;
    automaticTaskScore: number;
    companyScoreAvg: number;
    cooperation: boolean;
    employee: UserSimpleModel;
    gradeRecommendation: string;
    leader: UserSimpleModel;
    normalTasksScoreAvg: number;
    organizationScore: number;
    score: number;
    tasks: TaskItemModel[];
    textualEvaluation: string;
}
