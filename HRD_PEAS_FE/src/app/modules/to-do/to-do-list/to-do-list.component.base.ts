import { OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { TranslateService } from '@ngx-translate/core';
import { ToDoService } from '@src/app/services/to-do/to-do.service';
import { ReferenceType, ToDoStatus, ToDoType } from '@src/app/core/constants/constans';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { PageSizerConfig } from '@src/app/core/table-control/configs/page-sizer.config';
import { ToDoModel, ToDoSearchModel } from '@src/app/models/to-do/to-do.model';
import { BaseDatasModel } from '@src/app/models/base/base-datas.model';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';
import { SortDirection } from '@src/app/core/table-control/constants/sort-direction.contant';

export class ToDoListComponentBase extends BaseComponent implements OnInit {
  public referenceType = ReferenceType;
  public toDoType = ToDoType;
  public toDoTypes;
  public toDoStatus = ToDoStatus;
  public toDoStatuses;
  public search: ToDoSearchModel = new ToDoSearchModel();
  public tableOptions: TableOptions =
      new TableOptions( 'todo', this.router, PageSizerConfig.DEFAULT_PAGE_SIZE, this.search, true, true );
  public list: Array<ToDoModel> = new Array<ToDoModel>();

  public baseData: BaseDatasModel;

  constructor(protected router: Router,
              protected notification: NotificationBaseService,
              protected toDoService: ToDoService,
              protected translate: TranslateService,
              protected route: ActivatedRoute,
              protected dataTransferService: DataTransferService) {
    super();
    this.toDoTypes = Object.values(this.toDoType);
    this.toDoStatuses = Object.values(this.toDoStatus);

    this.tableOptions.sortedColumn = 'deadline';
    this.tableOptions.sortDirection = SortDirection.ASC;
  }

  ngOnInit() {
    if (this.dataTransferService.data) {
      this.search = this.dataTransferService.data;
      this.dataTransferService.data = null;
      this.tableOptions.saveSearch();
    }
    this.getList();
  }

  getList() {
    if (this.search.deadlineRange) {
      this.DateUtils.setEndOfDay( this.search.deadlineRange.max );
      this.DateUtils.setStartOfDay( this.search.deadlineRange.min );
    }
    if (this.search.doneRange) {
      this.DateUtils.setEndOfDay( this.search.doneRange.max );
      this.DateUtils.setStartOfDay( this.search.doneRange.min );
    }
    this.toDoService.getList( this.search, this.tableOptions ).subscribe(
        response => {
          this.list = response ? response.content : new Array();
          this.tableOptions.update( response ? response.totalElements : 0, this.list );
        }
    );
  }

  goToReferenceAction( item: ToDoModel) {
    if (this.toDoStatus.OPEN === item.status) {
      switch ( item.referenceType ) {
        case this.referenceType.EVALUATION: {
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
