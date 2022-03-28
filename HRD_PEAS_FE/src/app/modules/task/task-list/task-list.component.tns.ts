import { Component, OnInit } from '@angular/core';
import { TaskListComponentBase } from '@src/app/modules/task/task-list/task-list.component.base';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { ResourceService } from '@src/app/services/resource/resource.service';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@Component({
  selector: 'peas-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
  moduleId: module.id
})
export class TaskListComponent extends TaskListComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected taskService: TaskService,
              protected translate: TranslateService,
              protected activatedRoute: ActivatedRoute,
              protected resourceService: ResourceService,
              protected dataTransferService: DataTransferService) {
    super(router, notification, taskService, translate, activatedRoute, resourceService, dataTransferService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

}
