import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { TaskModel } from '@src/app/models/task/task.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { CommonUtils } from '@src/app/core/utils/common.util';

export class TaskViewComponentBase extends BaseComponent implements OnInit {
  taskId: string;
  data: TaskModel;

  constructor(protected router: Router,
              protected notification: NotificationBaseService,
              protected taskService: TaskService,
              protected translate: TranslateService,
              protected route: ActivatedRoute) {
    super();
  }

  ngOnInit() {
    this.route.params.subscribe( params => {
      if (CommonUtils.isNotEmpty(params.id)) {
        this.taskId = params.id;
        this.taskService.get(this.taskId).subscribe(
            response => {
              this.data = response;
            },
            error => {
              this.back();
            }
        );
      }
    });
  }

  copy() {
    this.taskService.copy(this.taskId).subscribe(
        response => {
          this.notification.success( this.translate.instant( 'notification.general.success_copy' ));
          this.router.navigate( [ this.SiteURLS.TASK.BASE + '/' + this.SiteURLS.TASK.EDIT, response.id ] );
        }
    );
  }

  evaluation() {
      if (CommonUtils.isNotEmpty(this.data.evaluationId)) {
          this.router.navigate( [ this.SiteURLS.RATING.BASE + '/' + this.SiteURLS.RATING.EVALUATION, this.data.evaluationId ] );
      }
  }
}
