import { Component, OnInit } from '@angular/core';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { PageSizerConfig } from '@src/app/core/table-control/configs/page-sizer.config';
import { Period, PeriodSearch } from '@src/app/models/settings/period';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { PeriodService } from '@src/app/services/settings/period.service';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { NotificationService } from '@src/app/core/notification/notification.service';

@Component({
  selector: 'peas-period-list',
  templateUrl: './period-list.component.html',
  styleUrls: ['./period-list.component.scss']
})
export class PeriodListComponent extends BaseComponent implements OnInit {
  public search: PeriodSearch = new PeriodSearch();
  public tableOptions: TableOptions =
      new TableOptions( 'periods', this.router, PageSizerConfig.DEFAULT_PAGE_SIZE, this.search, true, true );
  public list: Array<Period> = new Array<Period>();

  constructor(private router: Router,
              private notification: NotificationService,
              private translate: TranslateService,
              private periodService: PeriodService) {
    super();
  }

  ngOnInit() {
  }

  getList() {
    this.periodService.getList( this.search, this.tableOptions ).subscribe(
        response => {
          this.list = response ? response.content : new Array();
          this.tableOptions.update( response ? response.totalElements : 0, this.list );
        }
    );
  }

}
