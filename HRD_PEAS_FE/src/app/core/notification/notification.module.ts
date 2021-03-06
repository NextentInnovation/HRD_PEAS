import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationComponent } from './notification-components/notification.component';

@NgModule( {
  imports: [
    CommonModule
  ],
  declarations: [
    NotificationComponent
  ],
  entryComponents: [
    NotificationComponent
  ]
})
export class NotificationModule {
}
