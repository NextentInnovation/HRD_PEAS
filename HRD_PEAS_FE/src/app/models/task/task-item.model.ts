import { BaseModel } from '@src/app/models/base/base.model';
import { UserSimpleModel } from '@src/app/models/user/user.data.model';
import { DifficultyModel } from '@src/app/models/base/base-datas.model';

export class TaskItemModel extends BaseModel {
    createdDate: Date;
    deadline: Date;
    description: string;
    id: number;
    name: string;
    owner: UserSimpleModel;
    evaluationPercentage: number;
    score: number;
    startDate: Date;
    endDate: Date;
    status: string;
    taskType: string;
    evaluable: boolean;
    difficulty: DifficultyModel;
}
