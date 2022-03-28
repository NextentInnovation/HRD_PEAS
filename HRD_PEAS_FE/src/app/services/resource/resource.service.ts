import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { AutoComplateModel } from '@src/app/models/base/autocomplate.model';
import { BaseDatasModel } from '@src/app/models/base/base-datas.model';

export class ResourceService extends BaseService {

    constructor(private http: HttpClient) {
        super();
    }

    autoComplete(type: any, filter: string) {
        return this.http.put<AutoComplateModel<any>>( ApiURLS.AUTO_COMPLET,
            { filter: filter, limit: 200, autocompletType: type, autocomplet: type });
    }

    getBaseDatas() {
        return this.useCache(ApiURLS.BASE_DATAS, this.http.get<BaseDatasModel>( ApiURLS.BASE_DATAS ));
    }
}
