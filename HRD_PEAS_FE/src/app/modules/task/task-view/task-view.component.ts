import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TaskService } from '@src/app/services/task/task.service';
import { BsModalService } from 'ngx-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmModalComponent } from '@src/app/shared/modals/confirm-modal/confirm-modal.component';
import { TaskViewComponentBase } from '@src/app/modules/task/task-view/task-view.component.base';

@Component({
  selector: 'peas-task-view',
  templateUrl: './task-view.component.html',
  styleUrls: ['./task-view.component.scss']
})
export class TaskViewComponent extends TaskViewComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected taskService: TaskService,
              private modalService: BsModalService,
              protected translate: TranslateService,
              protected route: ActivatedRoute) {
    super(router, notification, taskService, translate, route);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  copy() {
    this.modalOptions.initialState = { title: this.translate.instant( 'task_edit.copy.confirm_modal.title' ),
      text: this.translate.instant( 'task_edit.copy.confirm_modal.sub_title' ) };
    this.modalService.show( ConfirmModalComponent, this.modalOptions ).content.confirm.subscribe(
        () => super.copy() );
  }
}
