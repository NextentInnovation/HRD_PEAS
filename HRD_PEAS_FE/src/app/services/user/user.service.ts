import { BaseService } from '@src/app/core/services/base.service';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { HttpClient } from '@angular/common/http';
import { UserDataModel } from '@src/app/models/user/user.data.model';

export class UserService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getMeUserData() {
    return this.http.get<UserDataModel>( ApiURLS.ME_USER_DATA );
  }
}
