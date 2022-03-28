import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ToDoRoutingModule } from '@src/app/modules/to-do/to-do-routing.module';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { ToDoListComponent } from '@src/app/modules/to-do/to-do-list/to-do-list.component';
import { ToDoService } from '@src/app/services/to-do/to-do.service';

@NgModule({
  declarations: [ToDoListComponent],
  imports: [
    CommonModule,
    ToDoRoutingModule,
    CoreSharedModule,
  ],
  providers: [
    ToDoService
  ],
})
export class ToDoModule { }
