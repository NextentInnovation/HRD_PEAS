import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { RatingRoutingModule } from '@src/app/modules/rating/rating-routing.module';
import { EvaluationComponent } from '@src/app/modules/rating/evaluation/evaluation.component';
import { EvaluationServices } from '@src/app/services/rating/evaluation.services';
import { RatingComponent } from '@src/app/modules/rating/rating/rating.component';
import { RatingServices } from '@src/app/services/rating/rating.services';
import { ReportModule } from '@src/app/modules/report/report.module';

@NgModule( {
    declarations: [ EvaluationComponent, RatingComponent ],
    imports: [
        CommonModule,
        RatingRoutingModule,
        CoreSharedModule,
        ReportModule
    ],
    providers: [
        EvaluationServices,
        RatingServices,
    ]
} )
export class RatingModule {
}
