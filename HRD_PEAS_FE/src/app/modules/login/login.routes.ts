import { Routes } from '@angular/router';

import { LoginComponent } from '@src/app/modules/login/login/login.component';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';

export const routes: Routes = [
    {
        path: '',
        component: LoginComponent,
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
