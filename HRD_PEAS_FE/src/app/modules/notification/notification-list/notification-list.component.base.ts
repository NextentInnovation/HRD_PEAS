import { OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { TranslateService } from '@ngx-translate/core';
import { NotificationIFService } from '@src/app/services/notification/notification.service';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { PageSizerConfig } from '@src/app/core/table-control/configs/page-sizer.config';
import { NotificationModel, NotificationSearchModel } from '@src/app/models/notification/notification.model';
import { ReferenceType, ToDoStatus } from '@src/app/core/constants/constans';
import { SortDirection } from '@src/app/core/table-control/constants/sort-direction.contant';

export class NotificationListComponentBase extends BaseComponent implements OnInit {
  public toDoStatus = ToDoStatus;

  public search: NotificationSearchModel = new NotificationSearchModel();
  public tableOptions: TableOptions =
      new TableOptions( 'notification', this.router, PageSizerConfig.DEFAULT_PAGE_SIZE, this.search, true, true );
  public list: Array<NotificationModel> = new Array<NotificationModel>();

  constructor(protected router: Router,
              protected notification: NotificationBaseService,
              protected notificationIFService: NotificationIFService,
              protected translate: TranslateService,
              protected route: ActivatedRoute) {
    super();

    this.tableOptions.sortedColumn = 'createdDate';
    this.tableOptions.sortDirection = SortDirection.DESC;
  }

  ngOnInit() {
    this.getList();
  }

  getList() {
    if (this.search.createdDateRange) {
      this.DateUtils.setEndOfDay( this.search.createdDateRange.max );
      this.DateUtils.setStartOfDay( this.search.createdDateRange.min );
    }
    this.notificationIFService.getList( this.search, this.tableOptions ).subscribe(
        response => {
          this.list = response ? response.content : new Array();
          this.tableOptions.update( response ? response.totalElements : 0, this.list );
        }
    );
  }

  goToReferenceAction( item: NotificationModel) {
    if (this.toDoStatus.OPEN === item.status) {
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
          this.notification.info( this.translate.instant('notification.click_event.unmanaged_type') );
          break;
        }
      }
    }
  }
}
