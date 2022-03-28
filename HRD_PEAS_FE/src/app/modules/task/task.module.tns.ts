import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';

import { TaskRoutingModule } from '@src/app/modules/task/task-routing.module';
import { NativeScriptCommonModule } from 'nativescript-angular/common';
import { TaskListComponent } from '@src/app/modules/task/task-list/task-list.component';
import { TaskEditComponent } from '@src/app/modules/task/task-edit/task-edit.component';
import { TaskService } from '@src/app/services/task/task.service';
import { ResourceService } from '@src/app/services/resource/resource.service';
import { TaskViewComponent } from '@src/app/modules/task/task-view/task-view.component';

@NgModule({
  declarations: [TaskListComponent, TaskEditComponent, TaskViewComponent],
  imports: [
    TaskRoutingModule,
    NativeScriptCommonModule
  ],
  providers: [
    TaskService,
    ResourceService
  ],
  schemas: [NO_ERRORS_SCHEMA]
})
export class TaskModule { }
