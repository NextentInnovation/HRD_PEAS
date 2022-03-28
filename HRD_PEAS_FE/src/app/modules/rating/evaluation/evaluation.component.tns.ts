import { Component, OnInit } from '@angular/core';
import { EvaluationComponentBase } from '@src/app/modules/rating/evaluation/evaluation.component.base';
import { ActivatedRoute, Router } from '@angular/router';
import { EvaluationServices } from '@src/app/services/rating/evaluation.services';
import { TranslateService } from '@ngx-translate/core';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@Component({
  selector: 'peas-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.scss'],
  moduleId: module.id
})
export class EvaluationComponent extends EvaluationComponentBase implements OnInit {

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected evaluationServices: EvaluationServices,
              protected translate: TranslateService,
              protected route: ActivatedRoute,
              protected dataTransferService: DataTransferService) {
    super(router, notification, evaluationServices, translate, route, dataTransferService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

}
