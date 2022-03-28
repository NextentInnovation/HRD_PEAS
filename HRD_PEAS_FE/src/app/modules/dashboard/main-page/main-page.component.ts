import { Component, OnInit } from '@angular/core';
import { MainPageComponentBase } from '@src/app/modules/dashboard/main-page/main-page.component.base';
import { Router } from '@angular/router';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';
import { ToDoService } from '@src/app/services/to-do/to-do.service';

@Component({
  selector: 'peas-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.scss']
})
export class MainPageComponent extends MainPageComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected taskService: TaskService,
              protected translate: TranslateService,
              protected dataTransferService: DataTransferService,
              protected toDoService: ToDoService) {
    super(router, notification, taskService, translate, dataTransferService, toDoService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

}
