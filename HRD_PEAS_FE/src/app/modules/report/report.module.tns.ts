import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';

import { ReportRoutingModule } from '@src/app/modules/report/report-routing.module';
import { NativeScriptCommonModule } from 'nativescript-angular/common';
import { EmployeeComponent } from '@src/app/modules/report/employee/employee.component';
import { EmployeesComponent } from '@src/app/modules/report/employees/employees.component';
import { EvaluationComponent } from '@src/app/modules/report/evaluation/evaluation.component';

@NgModule({
  declarations: [EmployeeComponent, EmployeesComponent, EvaluationComponent],
  imports: [
    ReportRoutingModule,
    NativeScriptCommonModule
  ],
  schemas: [NO_ERRORS_SCHEMA]
})
export class ReportModule { }
