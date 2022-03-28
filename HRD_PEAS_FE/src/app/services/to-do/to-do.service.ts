import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { PaginationListResponse } from '@src/app/models/base/pagination-list-response.interface';
import { finalize } from 'rxjs/operators';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';
import { ToDoModel, ToDoSearchModel } from '@src/app/models/to-do/to-do.model';

export class ToDoService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getList( search: ToDoSearchModel, tableOptions: TableOptions) {
    this.addLoadingIndicator( tableOptions.name );
    const uri = ApiURLS.TO_DO_LIST;
    return this.http.put<PaginationListResponse<ToDoModel>>( uri, BaseSearch.searchPagerMarge(search, tableOptions.getPagination()) )
               .pipe(finalize( () => this.removeLoadingIndicator( tableOptions.name ) ) );
  }
}
