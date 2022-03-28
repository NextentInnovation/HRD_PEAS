import { FactorModel } from '@src/app/models/base/base-datas.model';
import { TaskItemModel } from '@src/app/models/task/task-item.model';

export interface Selected {
    id: number;
    name: string;
    score: number;
}

export interface Factor {
    factor: FactorModel;
    required: boolean;
    selected: Selected;
}

export class EvaluationModel {
    deadline: Date;
    evaluatedStartDate: Date;
    factors: Factor[];
    id: number;
    note: string;
    task: TaskItemModel;
}
