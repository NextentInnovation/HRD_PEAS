import { OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { PageSizerConfig } from '@src/app/core/table-control/configs/page-sizer.config';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { TaskService } from '@src/app/services/task/task.service';
import { TaskItemModel } from '@src/app/models/task/task-item.model';
import { TaskListSearchModel } from '@src/app/models/task/task-list-search.model';
import { AutoCompletType, TaskStatuses } from '@src/app/core/constants/constans';
import { UserDataModel } from '@src/app/models/user/user.data.model';
import { StorageControl } from '@src/app/core/storage/storage.control';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { ResourceService } from '@src/app/services/resource/resource.service';
import { BaseDatasModel } from '@src/app/models/base/base-datas.model';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';
import { EvaluatorModel } from '@src/app/models/task/task.model';

export class TaskListComponentBase extends BaseComponent implements OnInit, OnDestroy {
    public TaskStatuses = TaskStatuses;
    public search: TaskListSearchModel = new TaskListSearchModel();
    public tableOptions: TableOptions =
        new TableOptions( 'tasks', this.router, PageSizerConfig.DEFAULT_PAGE_SIZE, this.search, true, true );
    public list: Array<TaskItemModel> = new Array<TaskItemModel>();

    public baseData: BaseDatasModel;
    public meUser: UserDataModel;
    public users: EvaluatorModel[];

    servie: Subscription;

    constructor( protected router: Router,
                 protected notification: NotificationBaseService,
                 protected taskService: TaskService,
                 protected translate: TranslateService,
                 protected activatedRoute: ActivatedRoute,
                 protected resourceService: ResourceService,
                 protected dataTransferService: DataTransferService) {
        super();
    }

    ngOnInit() {
        if (this.dataTransferService.data) {
            this.search = this.dataTransferService.data;
            this.dataTransferService.data = null;
            this.tableOptions.saveSearch();
        }
        this.getList();
        this.getUsers();
        this.meUser = StorageControl.getUserData();
        this.servie = this.resourceService.getBaseDatas().subscribe(
            response => {
                this.baseData = response;

                /**
                 *  Mivel ez nem kerül megjelenítésre, itt ki kell venni a törölt státuszt, hogy a lenyílóbe ne kerüljön felsorolásra
                 */
                this.baseData.taskstatuses = this.baseData.taskstatuses.filter(value => value !== TaskStatuses.DELETED);
            }
        );
    }

    ngOnDestroy() {
        this.servie.unsubscribe();
    }

    getList() {
        if (this.search.createdDateRange) {
            this.DateUtils.setEndOfDay( this.search.createdDateRange.max );
            this.DateUtils.setStartOfDay( this.search.createdDateRange.min );
        }
        if (this.search.deadlineRange) {
            this.DateUtils.setEndOfDay( this.search.deadlineRange.max );
            this.DateUtils.setStartOfDay( this.search.deadlineRange.min );
        }
        if (this.search.endDateRange) {
            this.DateUtils.setEndOfDay( this.search.endDateRange.max );
            this.DateUtils.setStartOfDay( this.search.endDateRange.min );
        }
        this.taskService.getList( this.search, this.tableOptions ).subscribe(
            response => {
                this.list = response ? response.content : new Array();
                this.tableOptions.update( response ? response.totalElements : 0, this.list );
            }
        );
    }

    getUsers( filter = '' ) {
        this.resourceService.autoComplete( AutoCompletType.USER, filter ).subscribe(
            response => {
                const userList = new Array<EvaluatorModel>();
                if ( response.content ) {
                    response.content.forEach( value => {
                        userList.push( new EvaluatorModel( value.id, value.description, value.name, value.email ) );
                    } );
                }
                this.users = userList;
            }
        );
    }

    delete(taskID: string) {
        this.taskService.delete( taskID ).subscribe(
            () => {
                this.notification.success( this.translate.instant( 'notification.general.success_delete' ) );
                this.getList();
            }
        );
    }

    copy(taskID: string) {
        this.taskService.copy( taskID ).subscribe(
            response => {
                this.notification.success( this.translate.instant( 'notification.general.success_copy' ) );
                this.router.navigate( [ this.SiteURLS.TASK.BASE + '/' + this.SiteURLS.TASK.EDIT, response.id ] );
            }
        );
    }
}


