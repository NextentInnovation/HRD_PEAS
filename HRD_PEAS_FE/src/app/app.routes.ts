import { Routes } from '@angular/router';

import { TestComponent } from '@src/app/test/test.component';
import { SiteURLS } from '@src/app/core/constants/urls/site-url.constant';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { MainWrapperComponent } from '@src/app/core/layouts/main-wrapper/main-wrapper.component';
import { AuthGuard } from '@src/app/core/guards/auth.guard';
import { Permissions } from '@src/app/core/constants/permissions.constants';
import { PermissionGuard } from '@src/app/core/guards/permission.guard';

export const routes: Routes = [
    {
        path: SiteURLS.LOGIN.BASE,
        loadChildren: () => import( './modules/login/login.module' ).then( mod => mod.LoginModule ),
    },
    {
        path: '',
        component: MainWrapperComponent,
        canActivate: [ AuthGuard ],
        children: [
            {
                path: SiteURLS.BASE.TEST_COMPONENTS,
                component: TestComponent,
            },
            {
                path: SiteURLS.NOTIFICATION.BASE,
                loadChildren: () => import( './modules/notification/notification.module' ).then( mod => mod.NotificationModule ),
            },
            {
                path: SiteURLS.REPORT.BASE,
                loadChildren: () => import( './modules/report/report.module' ).then( mod => mod.ReportModule ),
            },
            {
                path: SiteURLS.REPORT.PERSONAL_BASE,
                loadChildren: () => import( './modules/report/report.module' ).then( mod => mod.ReportModule ),
            },
            {
                path: SiteURLS.TASK.BASE,
                loadChildren: () => import( './modules/task/task.module' ).then( mod => mod.TaskModule ),
            },
            {
                path: SiteURLS.TO_DO.BASE,
                loadChildren: () => import( './modules/to-do/to-do.module' ).then( mod => mod.ToDoModule ),
            },
            {
                path: SiteURLS.SETTINGS.BASE,
                loadChildren: () => import( './modules/settings/settings.module' ).then( mod => mod.SettingsModule ),
            },
            {
                path: SiteURLS.RATING.BASE,
                loadChildren: () => import( './modules/rating/rating.module' ).then( mod => mod.RatingModule ),
            },
            {
                path: '',
                loadChildren: () => import( './modules/dashboard/dashboard.module' ).then( mod => mod.DashboardModule),
            },
        ]
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
