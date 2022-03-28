import { Component, OnInit } from '@angular/core';
import { TaskEditComponentBase } from '@src/app/modules/task/task-edit/task-edit.component.base';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { ResourceService } from '@src/app/services/resource/resource.service';

@Component({
  selector: 'peas-task-edit',
  templateUrl: './task-edit.component.html',
  styleUrls: ['./task-edit.component.scss'],
  moduleId: module.id
})
export class TaskEditComponent extends TaskEditComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected taskService: TaskService,
              protected translate: TranslateService,
              protected route: ActivatedRoute,
              protected resourceService: ResourceService) {
    super(router, notification, taskService, translate, route, resourceService);
  }

  ngOnInit() {
    super.ngOnInit();
  }
}
