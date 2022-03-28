import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { TaskItemModel } from '@src/app/models/task/task-item.model';
import { PaginationListResponse } from '@src/app/models/base/pagination-list-response.interface';
import { finalize } from 'rxjs/operators';
import { TaskModel } from '@src/app/models/task/task.model';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';

export class TaskService extends BaseService {

  constructor(private http: HttpClient) {
    super();
  }

  getList( search: any, tableOptions: TableOptions) {
    this.addLoadingIndicator( tableOptions.name );
    const uri = ApiURLS.TASK_LIST;
    return this.http.put<PaginationListResponse<TaskItemModel>>( uri, BaseSearch.searchPagerMarge(search, tableOptions.getPagination()) )
               .pipe(finalize( () => this.removeLoadingIndicator( tableOptions.name ) ) );
  }

  get(taskId: string) {
    return this.http.get<TaskModel>( ApiURLS.TASK_GET.replace('{taskId}', taskId));
}

  delete(taskId: string) {
    return this.http.delete<any>( ApiURLS.TASK_DELETE.replace('{taskId}', taskId));
  }

  set(taskId: string, task: any) {
    return this.http.put<TaskModel>( ApiURLS.TASK_EDIT.replace('{taskId}', taskId), task );
  }

  create(task: any) {
    return this.http.post<TaskModel>( ApiURLS.TASK_CREATE, task);
  }

  copy(taskId: string) {
    return this.http.post<TaskModel>( ApiURLS.TASK_COPY.replace('{taskId}', taskId), null);
  }

  startEvaluation( taskId: string) {
    return this.http.post<TaskModel>( ApiURLS.TASK_START_EVAULATION.replace('{taskId}', taskId), null);
  }

}
