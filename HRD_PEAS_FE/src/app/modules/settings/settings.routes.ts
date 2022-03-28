import { Routes } from '@angular/router';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { PeriodListComponent } from '@src/app/modules/settings/period-list/period-list.component';
import { PeriodEditComponent } from '@src/app/modules/settings/period-edit/period-edit.component';
import { SiteURLS } from '@src/app/core/constants/urls/site-url.constant';
import { Permissions } from '@src/app/core/constants/permissions.constants';
import { PermissionGuard } from '@src/app/core/guards/permission.guard';

export const routes: Routes = [
    {
        path: SiteURLS.SETTINGS.PERIOD_LIST,
        component: PeriodListComponent,
    },
    {
        path: SiteURLS.SETTINGS.PERIOD_EDIT,
        component: PeriodEditComponent,
        data: { permissions: [Permissions.HR, Permissions.BUSINESS_ADMIN] },
        canActivate: [ PermissionGuard ]
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
