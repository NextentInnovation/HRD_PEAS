import { BaseModel } from '@src/app/models/base/base.model';

export class UserSimpleModel extends BaseModel {
    active: boolean;
    companyName: string;
    email: string;
    fullName: string;
    id: number;
    mode: string;
    organization: string;
    userName: string;
}

export class UserDataModel extends UserSimpleModel {
    companyFullname: string;
    component: string[];
    initial: string;
    roleItems: string[];
    roles: string[];
}
