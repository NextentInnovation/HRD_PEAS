import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TaskRoutingModule } from '@src/app/modules/task/task-routing.module';
import { TaskListComponent } from '@src/app/modules/task/task-list/task-list.component';
import { TaskEditComponent } from '@src/app/modules/task/task-edit/task-edit.component';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { TaskService } from '@src/app/services/task/task.service';
import { ResourceService } from '@src/app/services/resource/resource.service';
import { TaskViewComponent } from '@src/app/modules/task/task-view/task-view.component';

@NgModule({
  declarations: [TaskListComponent, TaskEditComponent, TaskViewComponent],
  imports: [
    CommonModule,
    TaskRoutingModule,
    CoreSharedModule,
  ],
  providers: [
    TaskService,
    ResourceService
  ]
})
export class TaskModule { }
