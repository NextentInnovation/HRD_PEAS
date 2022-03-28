import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';
import { finalize } from 'rxjs/operators';
import { Period, PeriodSearch } from '@src/app/models/settings/period';
import { PaginationListResponse } from '@src/app/models/base/pagination-list-response.interface';
import { TaskModel } from '@src/app/models/task/task.model';

export class PeriodService extends BaseService {

    constructor( private http: HttpClient ) {
        super();
    }

    getList( search: PeriodSearch, tableOptions: TableOptions) {
        this.addLoadingIndicator( tableOptions.name );
        return this.http.put<PaginationListResponse<Period>>( ApiURLS.PERIOD_LIST, BaseSearch.searchPagerMarge(search, tableOptions.getPagination()) )
                   .pipe(finalize( () => this.removeLoadingIndicator( tableOptions.name ) ) );
    }

    create(period: Period) {
        return this.http.post<Period>( ApiURLS.PERIOD_CREATE, period);
    }

    get(periodId: string) {
        return this.http.get<Period>( ApiURLS.PERIOD_GET.replace('{periodId}', periodId));
    }

    edit(period: Period) {
        return this.http.put<Period>( ApiURLS.PERIOD_EDIT.replace('{periodId}', period.id.toString()), period);
    }

    delete(periodId: string) {
        return this.http.delete<any>( ApiURLS.PERIOD_DELETE.replace('{periodId}', periodId));
    }

    next() {
        return this.http.get<Period>( ApiURLS.PERIOD_GET_GENERATE);
    }
}
