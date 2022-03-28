import { Component, OnInit } from '@angular/core';
import { TaskListComponentBase } from '@src/app/modules/task/task-list/task-list.component.base';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TaskService } from '@src/app/services/task/task.service';
import { DeleteModalComponent } from '@src/app/shared/modals/delete-modal/delete-modal.component';
import { BsModalService } from 'ngx-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmModalComponent } from '@src/app/shared/modals/confirm-modal/confirm-modal.component';
import { ResourceService } from '@src/app/services/resource/resource.service';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@Component({
  selector: 'peas-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent extends TaskListComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected taskService: TaskService,
              private modalService: BsModalService,
              protected translate: TranslateService,
              protected activatedRoute: ActivatedRoute,
              protected resourceService: ResourceService,
              protected dataTransferService: DataTransferService) {
    super(router, notification, taskService, translate, activatedRoute, resourceService, dataTransferService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  openDeleteModal( taskID ) {
    this.modalService.show( DeleteModalComponent, this.modalOptions ).content.delete.subscribe( () => this.delete(taskID) );
  }

  openConfrimModal( taskID ) {
    this.modalOptions.initialState = { title: this.translate.instant( 'task_list.confirm_modal.title' ),
      text: this.translate.instant( 'task_list.confirm_modal.sub_title' ) };
    this.modalService.show( ConfirmModalComponent, this.modalOptions ).content.confirm.subscribe(() => this.copy(taskID) );
  }

}
