import { EventEmitter } from '@angular/core';
import { CommonUtils } from '../../core/utils/common.util';

export class NotificationConfig {

  static DURATION = 6000;
  static SUCCESS_CLASS = 'bg-success';
  static ERROR_CLASS = 'bg-danger';
  static INFO_CLASS = 'bg-info';
  verticalPosition: string;
  horizontalPosition: string;
  data: string;
  panelClass: string;

  constructor( message: string, panelClass: string ) {
    this.verticalPosition = 'top';
    this.horizontalPosition = 'right';

    if ( CommonUtils.isNotEmpty( message ) ) {
      this.setMessage( message );
    }

    if ( CommonUtils.isNotEmpty( panelClass ) ) {
      this.setPanelClass( panelClass );
    }
  }

  setMessage( message ) {
    this.data = message;
  }

  setPanelClass( panelClass ) {
    this.panelClass = panelClass;
  }
}

export class MessageDatas extends NotificationConfig {
  id: number;
  timeout = null;
  durationEnd: EventEmitter<MessageDatas> = new EventEmitter<MessageDatas>();
  animationClass = 'mdc-snackbar';

  constructor( notificationConfig: NotificationConfig, id: number ) {
    super( notificationConfig.data, notificationConfig.panelClass );
    if ( notificationConfig ) {
      this.data = notificationConfig.data;
      this.panelClass = notificationConfig.panelClass;
      this.horizontalPosition = notificationConfig.horizontalPosition;
      this.verticalPosition = notificationConfig.verticalPosition;
      this.id = id;
    }

    this.timeout = setTimeout( () => {
      this.durationEnd.emit( this );
    }, NotificationConfig.DURATION );
  }
}
