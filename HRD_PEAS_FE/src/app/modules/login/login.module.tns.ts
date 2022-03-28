import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';

import { LoginRoutingModule } from '@src/app/modules/login/login-routing.module';
import { NativeScriptCommonModule } from 'nativescript-angular/common';
import { LoginComponent } from '@src/app/modules/login/login/login.component';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';

@NgModule( {
    declarations: [
        LoginComponent
    ],
    imports: [
        LoginRoutingModule,
        CoreSharedModule,
        NativeScriptCommonModule
    ],
    schemas: [ NO_ERRORS_SCHEMA ]
} )
export class LoginModule {
}
