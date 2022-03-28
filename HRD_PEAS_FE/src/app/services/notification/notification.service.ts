import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { PaginationListResponse } from '@src/app/models/base/pagination-list-response.interface';
import { finalize } from 'rxjs/operators';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';
import { NotificationModel, NotificationSearchModel } from '@src/app/models/notification/notification.model';

export class NotificationIFService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getList( search: NotificationSearchModel, tableOptions: TableOptions) {
    this.addLoadingIndicator( tableOptions.name );
    const uri = ApiURLS.NOTIFICATION_LIST;
    return this.http.put<PaginationListResponse<NotificationModel>>( uri, BaseSearch.searchPagerMarge(search, tableOptions.getPagination()))
               .pipe(finalize( () => this.removeLoadingIndicator( tableOptions.name ) ) );
  }
}
