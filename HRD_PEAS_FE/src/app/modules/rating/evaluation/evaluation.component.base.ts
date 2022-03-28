import { OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { TranslateService } from '@ngx-translate/core';
import { EvaluationServices } from '@src/app/services/rating/evaluation.services';
import { EvaluationModel } from '@src/app/models/rating/evaluation.model';
import { HttpErrorResponse } from '@angular/common/http';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

export class EvaluationComponentBase extends BaseComponent implements OnInit {
    public data: EvaluationModel;

    private backType = 'basic';

    constructor( protected router: Router,
                 protected notification: NotificationBaseService,
                 protected evaluationServices: EvaluationServices,
                 protected translate: TranslateService,
                 protected route: ActivatedRoute,
                 protected dataTransferService: DataTransferService ) {
        super();
    }

    ngOnInit() {
        this.route.params.subscribe( params => {
            if ( CommonUtils.isNotEmpty( params.back ) ) {
                this.backType = params.back;
            }
            if ( CommonUtils.isNotEmpty( params.id ) ) {
                this.evaluationServices.get( params.id ).subscribe(
                    response => {
                        this.data = response;
                    },
                    ( err: HttpErrorResponse ) => {
                        // this.back();
                    }
                );
            } else {
                this.back();
            }
        } );
    }

    back() {
        if ( this.backType === 'main' ) {
            this.router.navigate( [ '/' ] );
        } else {
            super.back();
        }
    }

    saveEvaluation() {
        console.log( this.data );
        this.evaluationServices.set( this.data ).subscribe(
            response => {
                this.notification.success( this.translate.instant( 'evaluation.send.success' ) );
                this.dataTransferService.sendToDoComplete( this.data.id );
                this.back();
            }
        );
    }

}
