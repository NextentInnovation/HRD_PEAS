import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { NativeScriptCommonModule } from 'nativescript-angular/common';
import { EvaluationComponent } from '@src/app/modules/rating/evaluation/evaluation.component';
import { EvaluationServices } from '@src/app/services/rating/evaluation.services';
import { RatingRoutingModule } from '@src/app/modules/rating/rating-routing.module';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { RatingComponent } from '@src/app/modules/rating/rating/rating.component';
import { RatingServices } from '@src/app/services/rating/rating.services';
import { ReportModule } from '@src/app/modules/report/report.module';

@NgModule( {
    declarations: [ EvaluationComponent, RatingComponent ],
    imports: [
        NativeScriptCommonModule,
        RatingRoutingModule,
        CoreSharedModule,
        ReportModule,
        ReportModule,
        ReportModule,
    ],
    providers: [
        EvaluationServices,
        RatingServices
    ],
    schemas: [ NO_ERRORS_SCHEMA ]
} )
export class RatingModule {
}
