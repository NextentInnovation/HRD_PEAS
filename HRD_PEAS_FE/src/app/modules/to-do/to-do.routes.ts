import { Routes } from '@angular/router';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { ToDoListComponent } from '@src/app/modules/to-do/to-do-list/to-do-list.component';

export const routes: Routes = [
    {
        path: '',
        component: ToDoListComponent,
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
