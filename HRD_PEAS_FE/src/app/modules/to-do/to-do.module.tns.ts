import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';

import { ToDoRoutingModule } from '@src/app/modules/to-do/to-do-routing.module';
import { NativeScriptCommonModule } from 'nativescript-angular/common';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { ToDoListComponent } from '@src/app/modules/to-do/to-do-list/to-do-list.component';
import { ToDoService } from '@src/app/services/to-do/to-do.service';

@NgModule({
  declarations: [ToDoListComponent],
  imports: [
    ToDoRoutingModule,
    NativeScriptCommonModule,
    CoreSharedModule
  ],
  providers: [
    ToDoService
  ],
  schemas: [NO_ERRORS_SCHEMA]
})
export class ToDoModule { }
