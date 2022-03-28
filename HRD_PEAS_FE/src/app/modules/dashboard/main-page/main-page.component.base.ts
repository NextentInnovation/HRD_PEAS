import { OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { Router } from '@angular/router';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { TaskListSearchModel } from '@src/app/models/task/task-list-search.model';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { TaskItemModel } from '@src/app/models/task/task-item.model';
import { StorageControl } from '@src/app/core/storage/storage.control';
import { UserSimpleModel } from '@src/app/models/user/user.data.model';
import { ReferenceType, TaskStatuses, ToDoStatus } from '@src/app/core/constants/constans';
import { SortDirection } from '@src/app/core/table-control/constants/sort-direction.contant';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';
import { ToDoModel, ToDoSearchModel } from '@src/app/models/to-do/to-do.model';
import { ToDoService } from '@src/app/services/to-do/to-do.service';

export class MainPageComponentBase extends BaseComponent implements OnInit {
  public TaskStatuses = TaskStatuses;
  
  public inProgressTasksSearch: TaskListSearchModel = new TaskListSearchModel();
  public inProgressTasksTableOptions: TableOptions =
      new TableOptions( 'inProgressTasks', this.router, 10, this.inProgressTasksSearch, false, false );
  public inProgressTasksList: Array<TaskItemModel> = new Array<TaskItemModel>();

  public finishedTasksSearch: TaskListSearchModel = new TaskListSearchModel();
  public finishedTasksTableOptions: TableOptions =
      new TableOptions( 'finishedTasks', this.router, 10, this.finishedTasksSearch, false, false );
  public finishedTasksList: Array<TaskItemModel> = new Array<TaskItemModel>();

  public toDoSearch: ToDoSearchModel = new ToDoSearchModel();
  public toDoTableOptions: TableOptions =
      new TableOptions( 'todo', this.router, 10, this.toDoSearch, true, true );
  public toDoList: Array<ToDoModel> = new Array<ToDoModel>();

  protected meUser: UserSimpleModel;

  constructor(protected router: Router,
              protected notification: NotificationBaseService,
              protected taskService: TaskService,
              protected translate: TranslateService,
              protected dataTransferService: DataTransferService,
              protected toDoService: ToDoService) {
    super();
    this.finishedTasksTableOptions.isPagination = false;
    this.inProgressTasksTableOptions.isPagination = false;
    this.toDoTableOptions.isPagination = false;

    this.finishedTasksTableOptions.sortedColumn = 'startDate';
    this.finishedTasksTableOptions.sortDirection = SortDirection.DESC;

    this.inProgressTasksTableOptions.sortedColumn = 'deadline';
    this.inProgressTasksTableOptions.sortDirection = SortDirection.ASC;

    this.toDoTableOptions.sortedColumn = 'deadline';
    this.toDoTableOptions.sortDirection = SortDirection.ASC;
  }

  ngOnInit() {
    this.meUser = StorageControl.getUserData();
    this.getLists();
  }

  getLists() {
    this.inProgressTasksSearch.ownerId = this.meUser.id;
    this.inProgressTasksSearch.ownerName = this.meUser.fullName;
    this.inProgressTasksSearch.status = new Array<string>();
    this.inProgressTasksSearch.status.push(TaskStatuses.PARAMETERIZATION);
    this.inProgressTasksTableOptions.sortDirection = SortDirection.DESC;
    this.inProgressTasksTableOptions.sortedColumn = 'deadline';

    this.taskService.getList( this.inProgressTasksSearch, this.inProgressTasksTableOptions ).subscribe(
        response => {
          this.inProgressTasksList = response ? response.content : new Array();
        }
    );

    this.finishedTasksSearch.ownerId = this.meUser.id;
    this.finishedTasksSearch.ownerName = this.meUser.fullName;
    this.finishedTasksSearch.status = new Array<string>();
    this.finishedTasksSearch.status.push(TaskStatuses.CLOSED);
    this.finishedTasksSearch.status.push(TaskStatuses.EVALUATED);
    this.finishedTasksSearch.status.push(TaskStatuses.UNDER_EVALUATION);
    this.finishedTasksTableOptions.sortDirection = SortDirection.DESC;
    this.finishedTasksTableOptions.sortedColumn = 'deadline';

    this.taskService.getList( this.finishedTasksSearch, this.finishedTasksTableOptions ).subscribe(
        response => {
          this.finishedTasksList = response ? response.content : new Array();
        }
    );

    this.toDoSearch.status = new Array<string>();
    this.toDoSearch.status.push(ToDoStatus.OPEN);
    this.toDoService.getList( this.toDoSearch, this.toDoTableOptions).subscribe(
        response => {
          this.toDoList = response ? response.content : new Array();
          this.toDoTableOptions.update( response ? response.totalElements : 0, this.toDoList );
        }
    );
  }

  goToTaskListWithInProgressSearch() {
    this.dataTransferService.data = this.inProgressTasksSearch;
    this.router.navigate(['/' + this.SiteURLS.TASK.BASE]);
  }

  goToTaskListWithFinishedSearch() {
    this.dataTransferService.data = this.finishedTasksSearch;
    this.router.navigate(['/' + this.SiteURLS.TASK.BASE]);
  }

  goToToDoWithFinishedSearch() {
    this.dataTransferService.data = this.toDoSearch;
    this.router.navigate(['/' + this.SiteURLS.TO_DO.BASE]);
  }

  goToReferenceAction( item: ToDoModel) {
    if (ToDoStatus.OPEN === item.status) {
      switch ( item.referenceType ) {
        case ReferenceType.EVALUATION: {
          this.router.navigate( [ this.SiteURLS.RATING.BASE + '/' + this.SiteURLS.RATING.EVALUATION, item.reference ] );
          break;
        }
        case ReferenceType.RATING: {
          this.router.navigate( [ this.SiteURLS.RATING.BASE + '/' + this.SiteURLS.RATING.RATING, item.reference ] );
          break;
        }
        default: {
          this.notification.info( 'Az adott feladat nem elvégezhető művelet!' );
          break;
        }
      }
    }
  }

}
