import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TranslateService } from '@ngx-translate/core';
import { ReportServices } from '@src/app/services/report/report.services';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { EmployeeReportModel } from '@src/app/models/report/employee.report.model';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { SimplePeriodModel } from '@src/app/models/base/base-datas.model';
import { UserDataModel } from '@src/app/models/user/user.data.model';
import { StorageControl } from '@src/app/core/storage/storage.control';
import { TaskTypes } from '@src/app/core/constants/constans';

@Component({
  selector: 'peas-employee-report',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.scss']
})
export class EmployeeComponent extends BaseComponent implements OnInit {
  TaskTypes = TaskTypes;
  @Input() ratingId: string;

  public periodId: number;

  public userId: string;
  public data: EmployeeReportModel;

  public tableOptions: TableOptions =
      new TableOptions( 'employeeReport', this.router, 999, null, false, false );
  public periods: SimplePeriodModel[];
  public meUser: UserDataModel;
  public personal = false;

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected reportServices: ReportServices,
              protected translate: TranslateService,
              protected route: ActivatedRoute) {
    super();
    this.tableOptions.isPagination = false;
    this.meUser = StorageControl.getUserData();
  }

  ngOnInit() {
    let loaded = 0;
    if (CommonUtils.isEmpty(this.ratingId)) {
      this.route.data.subscribe(data => {
        if ( CommonUtils.isNotEmpty( data.personal ) && data.personal) {
          this.userId = this.meUser.id.toString();
          this.getPeriods();
          this.personal = true;
        } else {
          loaded++;
          if (loaded > 1) {
            this.back();
          }
        }
      });
      this.route.params.subscribe( params => {
        if ( CommonUtils.isNotEmpty( params.periodId ) ) {
          if (params.periodId !== 'active') {
            this.periodId = Number(params.periodId);
            if (CommonUtils.isNotEmpty(params.userId)) {
              this.userId = params.userId;
              this.getPeriods();
            }
            this.getReport();
          }
        } else {
          loaded++;
          if (loaded > 1) {
            this.back();
          }
        }
      } );
    } else {
      this.getReport();
    }
  }

  getReport() {
    this.reportServices.getEmployeeReport( this.ratingId, this.periodId, this.userId ).subscribe(
        response => {
          this.data = response;
        }
    );
  }

  getPeriods() {
    this.reportServices.getPeriods().subscribe(
        response => {
          this.periods = response;
          if (CommonUtils.isEmpty(this.periodId) && this.periods[0]) {
            this.periodId = this.periods[ 0 ].id;
            this.getReport();
          }
        }
    );
  }

  changePeriod() {
    // this.getReport();
    const changedUrl = this.router.url.split('/');
    changedUrl[changedUrl.length - 1] = this.periodId.toString();
    const newUrl = changedUrl.join('/');
    this.router.navigate([newUrl],  { replaceUrl: true });
  }

  isMe() {
    return this.data.employee.id === this.meUser.id;
  }
}
