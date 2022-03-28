import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { MessageDatas, NotificationConfig } from '../notification-message.model';

@Component( {
  selector: 'sas-notification',
  templateUrl: './notification.component.html',
  styleUrls: [ './notification.component.scss' ]
} )
export class NotificationComponent {
  public messageList = new Array<MessageDatas>();
  private id: number;

  constructor() {
    this.id = 0;
  }

  addNewMessage(message: NotificationConfig) {
    const newMessage = new MessageDatas(message, this.id++);
    this.messageList.push(newMessage);
    newMessage.animationClass = 'mdc-snackbar--open';
    newMessage.durationEnd.subscribe(value => {
      this.dismiss( value.id );
    });
  }

  dismiss( id: number ) {
    const message = this.messageList.find( value => value.id === id );
    if ( message ) {
      message.animationClass = 'mdc-snackbar--closing';
      message.durationEnd.unsubscribe();
      setTimeout( () => {
        this.messageList = this.messageList.filter( value => value.id !== id );
      }, 300 );
    }
  }

}
