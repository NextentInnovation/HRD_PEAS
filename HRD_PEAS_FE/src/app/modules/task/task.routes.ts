import { Routes } from '@angular/router';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { TaskListComponent } from '@src/app/modules/task/task-list/task-list.component';
import { TaskEditComponent } from '@src/app/modules/task/task-edit/task-edit.component';
import { SiteURLS } from '@src/app/core/constants/urls/site-url.constant';
import { TaskViewComponent } from '@src/app/modules/task/task-view/task-view.component';

export const routes: Routes = [
    {
        path: '',
        component: TaskListComponent,
    },
    {
        path: SiteURLS.TASK.EDIT + '/:id',
        component: TaskEditComponent,
    },
    {
        path: SiteURLS.TASK.CREATE,
        component: TaskEditComponent,
    },
    {
        path: SiteURLS.TASK.VIEW + '/:id',
        component: TaskViewComponent,
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
