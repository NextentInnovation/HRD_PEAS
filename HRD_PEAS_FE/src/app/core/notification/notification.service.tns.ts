import { Injectable } from '@angular/core';
import { NotificationConfig } from './notification-message.model';
import { NotificationModule } from './notification.module';
import { TranslateService } from '@ngx-translate/core';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';

@Injectable( {
  providedIn: NotificationModule
} )
export class NotificationService implements NotificationBaseService {

  constructor( private translate: TranslateService) {
  }

  success( message ) {
    const config: NotificationConfig = new NotificationConfig( message, NotificationConfig.SUCCESS_CLASS );
    alert(message);
  }


  info( message ) {
    const config: NotificationConfig = new NotificationConfig( message, NotificationConfig.INFO_CLASS );
    alert(message);
  }

  error( error ) {
    const config: NotificationConfig = new NotificationConfig( error, NotificationConfig.ERROR_CLASS );
    alert(error);
  }
}

