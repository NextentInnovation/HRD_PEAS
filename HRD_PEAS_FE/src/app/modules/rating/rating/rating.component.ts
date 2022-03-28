import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TranslateService } from '@ngx-translate/core';
import { RatingServices } from '@src/app/services/rating/rating.services';
import { RatingSendModel } from '@src/app/models/rating/rating.model';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@Component({
  selector: 'peas-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss']
})
export class RatingComponent extends BaseComponent implements OnInit {
  public data: RatingSendModel = new RatingSendModel();
  ratingId: string;
    private backType = 'basic';

  constructor(protected router: Router,
              protected notification: NotificationService,
              protected ratingServices: RatingServices,
              protected translate: TranslateService,
              protected route: ActivatedRoute,
              private dataTransferService: DataTransferService) {
    super();
    this.route.params.subscribe( params => {
      if (CommonUtils.isNotEmpty(params.id)) {
       this.ratingId = params.id;
      } else {
        this.back();
      }
    });
  }

  ngOnInit() {
  }

  save() {
    this.ratingServices.set(this.data, this.ratingId).subscribe(
        success => {
          this.notification.success('Mínősítés sikeresen leadva!');
          this.dataTransferService.sendToDoComplete(this.ratingId);
          this.back();
        });
  }

    back() {
        if ( this.backType === 'main' ) {
            this.router.navigate( [ '/' ] );
        } else {
            super.back();
        }
    }
}
