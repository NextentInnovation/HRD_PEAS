import { Routes } from '@angular/router';

import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { MainPageComponent } from '@src/app/modules/dashboard/main-page/main-page.component';

export const routes: Routes = [
    {
        path: '',
        component: MainPageComponent,
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
