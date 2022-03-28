import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { ReportServices } from '@src/app/services/report/report.services';
import { TranslateService } from '@ngx-translate/core';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { EvaluationReportModel } from '@src/app/models/report/evaluation.report.model';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { UserDataModel } from '@src/app/models/user/user.data.model';
import { StorageControl } from '@src/app/core/storage/storage.control';

@Component( {
    selector: 'peas-evaluation',
    templateUrl: './evaluation.component.html',
    styleUrls: [ './evaluation.component.scss' ]
} )
export class EvaluationComponent extends BaseComponent implements OnInit {

    public taskId: string;

    public data: EvaluationReportModel;
    public tableOptions: TableOptions =
        new TableOptions( 'evaluationReport', this.router, 999, null, false, false );

    public meUser: UserDataModel;

    private isAnonymized = true;

    constructor( protected router: Router,
                 protected notification: NotificationService,
                 protected reportServices: ReportServices,
                 protected translate: TranslateService,
                 protected route: ActivatedRoute ) {
        super();
        this.tableOptions.isPagination = false;
        this.meUser = StorageControl.getUserData();
    }

    ngOnInit() {
        this.route.params.subscribe( params => {
            console.log(params);
            if ( CommonUtils.isNotEmpty( params.taskId ) ) {
                this.taskId = params.taskId;
                this.getReport();
            } else {
                this.back();
            }
        });
    }

    getReport() {
        this.reportServices.getEvaluationReport( this.taskId ).subscribe(
            response => {
                this.data = response;
                if (this.isMe()) {
                    this.isAnonymized = false;
                }
            }
        );
    }

    isMe() {
        return this.data.employee.id === this.meUser.id;
    }

    isAnonymously(evaluatorName = '') {
        return this.isAnonymized || !(!this.isMe() || CommonUtils.isNotEmpty(evaluatorName));
    }

    tiggleNames() {
        this.isAnonymized = !this.isAnonymized;
    }
}
