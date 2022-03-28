import { Component, OnInit } from '@angular/core';
import { ToDoListComponentBase } from '@src/app/modules/to-do/to-do-list/to-do-list.component.base';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { ToDoService } from '@src/app/services/to-do/to-do.service';
import { TranslateService } from '@ngx-translate/core';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@Component({
  selector: 'peas-to-do-list',
  templateUrl: './to-do-list.component.html',
  styleUrls: ['./to-do-list.component.scss'],
  moduleId: module.id
})
export class ToDoListComponent extends ToDoListComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected toDoService: ToDoService,
              protected translate: TranslateService,
              protected route: ActivatedRoute,
              protected dataTransferService: DataTransferService ) {
    super( router, notification, toDoService, translate, route, dataTransferService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

}
