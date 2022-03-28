import { Component, OnInit } from '@angular/core';
import { LoginComponentBase } from '@src/app/modules/login/login/login.component.base';
import { LoginService } from '@src/app/services/login/login.service';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { UserService } from '@src/app/services/user/user.service';
import { Router } from '@angular/router';

@Component( {
    selector: 'peas-login',
    templateUrl: './login.component.html',
    styleUrls: [ './login.component.scss' ]
} )
export class LoginComponent extends LoginComponentBase implements OnInit {

    constructor(protected loginService: LoginService,
                protected notificitaion: NotificationService,
                protected userService: UserService,
                protected router: Router) {
        super(loginService, notificitaion, userService, router);
    }

    ngOnInit() {
    }

}
