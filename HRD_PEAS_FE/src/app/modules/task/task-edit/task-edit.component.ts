import { Component, OnInit } from '@angular/core';
import { TaskEditComponentBase } from '@src/app/modules/task/task-edit/task-edit.component.base';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { BsModalService } from 'ngx-bootstrap';
import { ConfirmModalComponent } from '@src/app/shared/modals/confirm-modal/confirm-modal.component';
import { ResourceService } from '@src/app/services/resource/resource.service';

@Component({
  selector: 'peas-task-edit',
  templateUrl: './task-edit.component.html',
  styleUrls: ['./task-edit.component.scss']
})
export class TaskEditComponent extends TaskEditComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected taskService: TaskService,
              private modalService: BsModalService,
              protected translate: TranslateService,
              protected route: ActivatedRoute,
              protected resourceService: ResourceService) {
    super(router, notification, taskService, translate, route, resourceService);
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
