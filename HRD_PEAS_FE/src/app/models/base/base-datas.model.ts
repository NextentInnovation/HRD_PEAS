import { BaseModel } from '@src/app/models/base/base.model';
import { UserDataModel, UserSimpleModel } from '@src/app/models/user/user.data.model';

export class BaseDatasModel extends BaseModel {
    activePeriod: PeriodModel;
    companyVirtues: CompanyVirtueModel[];
    currentUser: UserDataModel;
    difficulties: DifficultyModel[]; // Nehézségek felsorolása
    evaluationstatuses: string[]; // Értékelés státuszok
    factors: FactorModel[]; // Értékelési szempontok
    leader?: UserDataModel; // Vezető
    leaderVirtues: LeaderVirtueModel[];
    parameters: object;
    periodstatuses: string[];
    taskstatuses: string[]; // Task státuszok
    tasktypes: string[]; // Task típusok
}

export class CompanyVirtueModel {
    id: number;
    value: string;
}

export class DifficultyModel {
    description: string;
    id: number;
    multiplier: number;
    name: string;
}

export class LeaderVirtueModel {
    id: number;
    own: boolean;
    owner: UserSimpleModel;
    value: string;
}


export class FactorOptionModel {
    id: number;
    name: string;
    score: number;
}

export class FactorSimpleModel {
    id: number;
    name: string;
}

export class FactorModel extends FactorSimpleModel {
    required: boolean;
    options: FactorOptionModel[];
}

export class SimplePeriodModel {
    id: number; // Periódus azonosító ,
    name: string; // Periódus neve ,
}

export class PeriodModel extends SimplePeriodModel {
    closedUser?: string; // Lezáró felhasználó neve ,
    endDate?: string; // Periódus vége, akkor van csak kitöltve, amikor lezárták a periódust ,
    startDate?: Date; // Periódus kezdete
    ratingEndDate?: Date;
    status: string;
}
