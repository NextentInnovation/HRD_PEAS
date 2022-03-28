import { Routes } from '@angular/router';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { EmployeeComponent } from '@src/app/modules/report/employee/employee.component';
import { SiteURLS } from '@src/app/core/constants/urls/site-url.constant';
import { EvaluationComponent } from '@src/app/modules/report/evaluation/evaluation.component';
import { EmployeesComponent } from '@src/app/modules/report/employees/employees.component';
import { PermissionGuard } from '@src/app/core/guards/permission.guard';
import { Permissions } from '@src/app/core/constants/permissions.constants';

export const routes: Routes = [
    {
        path: '',
        component: EmployeesComponent,
        data: { permissions: [Permissions.HR, Permissions.LEADER] },
        canActivate: [ PermissionGuard ]
    },
    {
        path: SiteURLS.REPORT.EVALUATED + '/:periodId',
        component: EmployeeComponent,
        data: { personal: true }
    },
    {
        path: SiteURLS.REPORT.EVALUATED_TASK + '/:taskId',
        component: EvaluationComponent,
    },
    {
        path: SiteURLS.REPORT.EMPLOYEE + '/:userId/:periodId',
        component: EmployeeComponent,
        data: { permissions: [Permissions.HR, Permissions.LEADER] },
        canActivate: [ PermissionGuard ]
    },
    {
        path: SiteURLS.REPORT.EMPLOYEES,
        component: EmployeesComponent,
        data: { permissions: [Permissions.HR, Permissions.LEADER] },
        canActivate: [ PermissionGuard ]
    },
    {
        path: SiteURLS.REPORT.EVALUATION + '/:taskId',
        component: EvaluationComponent,
        data: { permissions: [Permissions.HR, Permissions.LEADER] },
        canActivate: [ PermissionGuard ]
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
