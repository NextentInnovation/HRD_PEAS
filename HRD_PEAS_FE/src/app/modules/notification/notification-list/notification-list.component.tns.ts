import { Component, OnInit } from '@angular/core';
import { NotificationListComponentBase } from '@src/app/modules/notification/notification-list/notification-list.component.base';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { NotificationIFService } from '@src/app/services/notification/notification.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'peas-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss'],
  moduleId: module.id
})
export class NotificationListComponent extends NotificationListComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected notificationIFService: NotificationIFService,
              protected translate: TranslateService,
              protected route: ActivatedRoute) {
    super(router, notification, notificationIFService, translate, route);
  }

  ngOnInit() {
    super.ngOnInit();
  }
}
