import { Routes } from '@angular/router';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { NotificationListComponent } from '@src/app/modules/notification/notification-list/notification-list.component';

export const routes: Routes = [
    {
        path: '',
        component: NotificationListComponent,
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
