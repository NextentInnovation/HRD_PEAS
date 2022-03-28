import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { ReportServices } from '@src/app/services/report/report.services';
import { TranslateService } from '@ngx-translate/core';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { SimplePeriodModel } from '@src/app/models/base/base-datas.model';
import {
    EmployeesPaginationReportModel,
    EmployeesReportSearchModel
} from '@src/app/models/report/employees.report.model';
import { UserDataModel } from '@src/app/models/user/user.data.model';
import { EvaluatorModel } from '@src/app/models/task/task.model';
import { StorageControl } from '@src/app/core/storage/storage.control';
import { AutoCompletType } from '@src/app/core/constants/constans';
import { ResourceService } from '@src/app/services/resource/resource.service';
import { CommonUtils } from '@src/app/core/utils/common.util';

@Component( {
    selector: 'peas-employees',
    templateUrl: './employees.component.html',
    styleUrls: [ './employees.component.scss' ]
} )
export class EmployeesComponent extends BaseComponent implements OnInit {
    public data: EmployeesPaginationReportModel;
    public search: EmployeesReportSearchModel = new EmployeesReportSearchModel();
    public tableOptions: TableOptions =
        new TableOptions( 'employeesReport', this.router, 999, this.search, true, true );

    public periods: SimplePeriodModel[];
    public meUser: UserDataModel;
    public users: EvaluatorModel[];

    constructor( protected router: Router,
                 protected notification: NotificationService,
                 protected reportServices: ReportServices,
                 protected translate: TranslateService,
                 protected route: ActivatedRoute,
                 protected resourceService: ResourceService ) {
        super();
    }

    ngOnInit() {
        const previousPeriod = this.router.routerState.snapshot.root.queryParamMap.get( 'period' );
        if ( CommonUtils.isNotEmpty(previousPeriod) && Number.isInteger(Number(previousPeriod))) {
           this.search.periodId = Number(previousPeriod);
        }
        this.getPeriods();
        this.getUsers();
        this.meUser = StorageControl.getUserData();
    }

    getReport() {
        this.reportServices.getEmployeesReport( this.search, this.tableOptions ).subscribe(
            response => {
                this.data = response;
                this.tableOptions.update( this.data.totalElements, this.data.content );
            }
        );
    }

    getPeriods() {
        this.reportServices.getPeriods().subscribe(
            response => {
                this.periods = response;
                if ( CommonUtils.isEmpty( this.search.periodId ) && this.periods[ 0 ] ) {
                    this.search.periodId = this.periods[ 0 ].id;
                }
                this.getReport();
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

    changePeriod() {
        this.router.navigate( [ CommonUtils.getBaseUrl( this.router.url ) ], {
            queryParamsHandling: 'merge',
            queryParams: { period: this.search.periodId },
            replaceUrl: true,
        } );
        this.getReport();
    }

}
