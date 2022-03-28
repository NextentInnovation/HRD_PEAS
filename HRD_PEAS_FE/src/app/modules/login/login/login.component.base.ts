import { OnInit } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { LoginService } from '@src/app/services/login/login.service';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { AuthenticationRequest } from '@src/app/models/authentication/authentication.model';
import { StorageControl } from '@src/app/core/storage/storage.control';
import { UserService } from '@src/app/services/user/user.service';
import { Router } from '@angular/router';
import { PermissionUtils } from '@src/app/core/utils/permission.util';

export class LoginComponentBase extends BaseComponent implements OnInit {
    userName: string;
    password: string;

    constructor( protected loginService: LoginService,
                 protected notification: NotificationBaseService,
                 protected userService: UserService,
                 protected router: Router ) {
        super();
    }

    ngOnInit() {
    }

    login() {
        if ( this.CommonUtils.isNotEmpty( this.userName ) && this.CommonUtils.isNotEmpty( this.password ) ) {
            const request = new AuthenticationRequest( this.userName, this.password );
            this.loginService.login( request ).subscribe(
                response => {
                    const res: string = response;
                    StorageControl.setToken( res.slice( 1, res.length - 1 ) );
                    this.userService.getMeUserData().subscribe(
                        me => {
                            StorageControl.setUsername( me.userName );
                            StorageControl.setUserData( me );
                            StorageControl.setPermission( me.roles );
                            PermissionUtils.forceLoadUserPermissions();
                            // this.notification.success( 'Sikeres bejelentkezés!' );
                            this.router.navigate( [ '/' ] );
                        }
                    );
                },
                error => {
                    console.log( error );
                    this.notification.error( 'Sikertelen bejelentkezés!' );
                },
            );
        }
    }

}
