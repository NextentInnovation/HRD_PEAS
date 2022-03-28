import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { NativeScriptCommonModule } from 'nativescript-angular/common';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { MainPageComponent } from '@src/app/modules/dashboard/main-page/main-page.component';
import { ToDoService } from '@src/app/services/to-do/to-do.service';
import { TaskService } from '@src/app/services/task/task.service';

@NgModule( {
    declarations: [ MainPageComponent ],
    imports: [
        NativeScriptCommonModule,
        CoreSharedModule
    ],
    providers: [
        ToDoService,
        TaskService
    ],
    schemas: [ NO_ERRORS_SCHEMA ]
} )
export class DashboardModule {
}
