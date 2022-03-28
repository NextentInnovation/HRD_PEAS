import { UserSimpleModel } from '@src/app/models/user/user.data.model';
import { FactorSimpleModel, PeriodModel } from '@src/app/models/base/base-datas.model';
import { TaskItemModel } from '@src/app/models/task/task-item.model';

export class EvaluationReportModel {
    asLeaderOrganization: string;
    asLeaderOrganizationScore: number;
    automaticTaskScore: number;
    companyScoreAvg: number;
    cooperation: boolean;
    employee: UserSimpleModel;
    factorScoreAvgs: number[];
    factors: FactorSimpleModel[];
    gradeRecommendation: string;
    leader: UserSimpleModel;
    normalTasksScoreAvg: number;
    organizationScore: number;
    period: PeriodModel;
    rows: Row[];
    score: number;
    task: TaskItemModel;
    textualEvaluation: string;
}

export interface Row {
    evaluatorName: string;
    factorScores: number[];
    note: string;
    scoreAvg: number;
}
