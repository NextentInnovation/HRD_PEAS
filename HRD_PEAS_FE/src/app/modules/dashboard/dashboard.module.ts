import { NgModule } from '@angular/core';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { MainPageComponent } from '@src/app/modules/dashboard/main-page/main-page.component';
import { TaskService } from '@src/app/services/task/task.service';
import { CommonModule } from '@angular/common';
import { DashboardRoutingModule } from '@src/app/modules/dashboard/dashboard-routing.module';
import { ToDoService } from '@src/app/services/to-do/to-do.service';

@NgModule( {
    declarations: [ MainPageComponent ],
    imports: [
        CommonModule,
        DashboardRoutingModule,
        CoreSharedModule,
    ],
    providers: [
        TaskService,
        ToDoService
    ]
} )
export class DashboardModule {
}
