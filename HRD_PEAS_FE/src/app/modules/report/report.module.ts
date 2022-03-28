import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportRoutingModule } from '@src/app/modules/report/report-routing.module';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { EmployeeComponent } from '@src/app/modules/report/employee/employee.component';
import { EmployeesComponent } from '@src/app/modules/report/employees/employees.component';
import { EvaluationComponent } from '@src/app/modules/report/evaluation/evaluation.component';
import { ReportServices } from '@src/app/services/report/report.services';
import { ResourceService } from '@src/app/services/resource/resource.service';

@NgModule( {
    declarations: [ EmployeeComponent, EmployeesComponent, EvaluationComponent ],
    imports: [
        CommonModule,
        ReportRoutingModule,
        CoreSharedModule,
    ],
    exports: [
        EmployeeComponent
    ],
    providers: [
        ReportServices,
        ResourceService
    ]
})
export class ReportModule { }
