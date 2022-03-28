import { Component, OnInit } from '@angular/core';
import { TestComponentBase } from '@src/app/test/test.component.base';
import { TestService } from '@src/app/test/test.service';
import { Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';

@Component({
  selector: 'peas-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss'],
})
export class TestComponent extends TestComponentBase implements OnInit {

  constructor(protected testService: TestService,
              protected router: Router,
              protected notification: NotificationService) {
    super(testService, router, notification);
  }

  ngOnInit() {
    super.ngOnInit();
    this.title += ' WEB';
  }

    popAlert( str: string ) {
        alert(str);
    }

    log( str: string ) {
      console.log(str);
    }
}
