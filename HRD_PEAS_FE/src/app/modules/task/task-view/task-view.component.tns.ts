import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { TaskViewComponentBase } from '@src/app/modules/task/task-view/task-view.component.base';

@Component({
  selector: 'peas-task-view',
  templateUrl: './task-view.component.html',
  styleUrls: ['./task-view.component.scss'],
  moduleId: module.id
})
export class TaskViewComponent extends TaskViewComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected taskService: TaskService,
              protected translate: TranslateService,
              protected route: ActivatedRoute) {
    super(router, notification, taskService, translate, route);
  }

  ngOnInit() {
    super.ngOnInit();
  }
}
