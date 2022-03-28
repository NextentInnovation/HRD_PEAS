import { ApplicationRef, ComponentFactoryResolver, ComponentRef, EmbeddedViewRef, Injectable, Injector } from '@angular/core';
import { NotificationConfig } from './notification-message.model';
import { NotificationModule } from './notification.module';
import { NotificationComponent } from './notification-components/notification.component';
import { TranslateService } from '@ngx-translate/core';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';

@Injectable( {
  providedIn: NotificationModule
} )
export class NotificationService implements NotificationBaseService {
  dialogComponentRef: ComponentRef<NotificationComponent>;

  constructor( private componentFactoryResolver: ComponentFactoryResolver,
               private appRef: ApplicationRef,
               private injector: Injector,
               private translate: TranslateService) {
  }

  appendNotificationComponentToBody() {
    if ( !this.dialogComponentRef ) {
      const componentFactory = this.componentFactoryResolver.resolveComponentFactory( NotificationComponent );
      const componentRef = componentFactory.create( this.injector );
      this.appRef.attachView( componentRef.hostView );

      const domElem = (componentRef.hostView as EmbeddedViewRef<any>).rootNodes[ 0 ] as HTMLElement;
      document.body.appendChild( domElem );

      this.dialogComponentRef = componentRef;
    }
  }

  private removeNotificationComponentFromBody() {
    this.appRef.detachView( this.dialogComponentRef.hostView );
    this.dialogComponentRef.destroy();
  }

  success( message ) {
    const config: NotificationConfig = new NotificationConfig( message, NotificationConfig.SUCCESS_CLASS );
    this.appendNotificationComponentToBody();
    this.dialogComponentRef.instance.addNewMessage( config );
  }


  info( message ) {
    const config: NotificationConfig = new NotificationConfig( message, NotificationConfig.INFO_CLASS );
    this.appendNotificationComponentToBody();
    this.dialogComponentRef.instance.addNewMessage( config );
  }

  error( error ) {
    this.appendNotificationComponentToBody();
    if ( error.error instanceof Array ) {
      error.error.forEach( errorMessage => {
        const config = new NotificationConfig(
            errorMessage.message || this.translate.instant('global.error.general.text'), NotificationConfig.ERROR_CLASS );
        this.dialogComponentRef.instance.addNewMessage( config );
      } );
    } else {
      const config = new NotificationConfig(
          error.message || error || this.translate.instant('global.error.general.text'), NotificationConfig.ERROR_CLASS );
      this.dialogComponentRef.instance.addNewMessage( config );
    }
  }
}

