import { NgModule } from '@angular/core';

import { LoginRoutingModule } from './login-routing.module';
import { LoginComponent } from './login/login.component';
import { CoreSharedModule } from '../../shared/modules/core-shared.module';
import { LoginService } from '@src/app/services/login/login.service';
import { UserService } from '@src/app/services/user/user.service';

@NgModule( {
    declarations: [
        LoginComponent,
    ],
    imports: [
        LoginRoutingModule,
        CoreSharedModule,
    ],
    exports: [
    ],
    providers: [
        LoginService,
        UserService
    ]

} )
export class LoginModule {
}
