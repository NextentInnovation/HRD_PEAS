import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotificationRoutingModule } from '@src/app/modules/notification/notification-routing.module';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { NotificationListComponent } from '@src/app/modules/notification/notification-list/notification-list.component';
import { NotificationIFService } from '@src/app/services/notification/notification.service';

@NgModule({
  declarations: [NotificationListComponent],
  imports: [
    CommonModule,
    NotificationRoutingModule,
    CoreSharedModule,
  ],
  providers: [
    NotificationIFService
  ]
})
export class NotificationModule { }
