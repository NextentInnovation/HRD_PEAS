import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';

import { NotificationRoutingModule } from '@src/app/modules/notification/notification-routing.module';
import { NativeScriptCommonModule } from 'nativescript-angular/common';
import { NotificationListComponent } from '@src/app/modules/notification/notification-list/notification-list.component';
import { NotificationIFService } from '@src/app/services/notification/notification.service';

@NgModule({
  declarations: [NotificationListComponent],
  imports: [
    NotificationRoutingModule,
    NativeScriptCommonModule
  ],
  providers: [
    NotificationIFService
  ],
  schemas: [NO_ERRORS_SCHEMA]
})
export class NotificationModule { }
