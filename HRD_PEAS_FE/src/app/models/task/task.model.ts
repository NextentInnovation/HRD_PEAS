import { BaseModel } from '@src/app/models/base/base.model';
import { UserSimpleModel } from '@src/app/models/user/user.data.model';
import {
    CompanyVirtueModel,
    DifficultyModel,
    FactorModel,
    LeaderVirtueModel,
    PeriodModel
} from '../base/base-datas.model';

export class TaskModel extends BaseModel {
    id: number;
    companyVirtues: CompanyVirtueModel[];  // Vezetői értékek
    createdDate: Date; // Létrehozás ideje
    deadline: Date; // Értékelés határideje
    description: string; // Feladat leírása
    difficulty: DifficultyModel | any; // Feleadat nehézsége
    evaluationId: number; // Értékelés azonosítója
    evaluationPercentage: number; // Értkeltség százaléka
    evaluators: EvaluatorModel[]; // Értékelők
    leaderVirtues: LeaderVirtueModel[]; // Vezetői értékek listája
    name: string; // Feladat neve
    owner: UserSimpleModel; // Letrehozó tulajdonos
    period: PeriodModel; // Melyik periódusba tartozik
    score: number; // Értékelés eredménye
    startDate: Date; // Értékelés indításának az ideje
    status: string; // Feladat státusza
    taskType: string; // Feladat típusa
    taskfactors: FactorModel[]; // Taskhoz rendelt értékelési szempontok
    endDate: Date; // Értékelés vége

    constructor() {
        super();
        this.companyVirtues = new Array<CompanyVirtueModel>();
        this.difficulty = new DifficultyModel();
        this.evaluators = new Array<EvaluatorModel>();
        this.leaderVirtues = new Array<LeaderVirtueModel>();
        this.owner = new UserSimpleModel();
        this.period = new PeriodModel();
        this.taskfactors = new Array<FactorModel>();
    }
}

export class EvaluatorModel {
    evaluator: UserSimpleModel;
    evaulatedDate: Date;
    score: number;
    status: string;

    constructor(id?: number, fullName?: string, userName?: string, email?: string) {
        this.evaluator = new UserSimpleModel();
        if (id) {
            this.evaluator.id = id;
        }
        if (fullName) {
            this.evaluator.fullName = fullName;
        }
        if (userName) {
            this.evaluator.userName = userName;
        }
        if (email) {
            this.evaluator.email = email;
        }
    }
}

