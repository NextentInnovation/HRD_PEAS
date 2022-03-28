import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { TableControlModule } from '../../core/table-control/table-control.module';
import { LayoutSharedModule } from './layout-shared.module';
import { DpDatePickerModule } from 'ng2-date-picker';
import { FormatDateModelDirective } from '../../directives/format-date-model/format-date-model.directive';
import { CommonModule, HashLocationStrategy, LocationStrategy } from '@angular/common';
import { NumberCheckerDirective } from '../../directives/number-checker/number-checker.directive';
import { TranslateModule } from '@ngx-translate/core';
import { AuthGuard } from '../../core/guards/auth.guard';
import { PermissionGuard } from '../../core/guards/permission.guard';
import { SetFocusDirective } from '../../directives/set-focus/set-focus.directive';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { MaterialSharedModule } from '@src/app/shared/modules/material-shared.module';
import { PrimeNgSharedModule } from '@src/app/shared/modules/primeng-shared.module';
import { BootstrapSharedModule } from '@src/app/shared/modules/bootstrap-shared.module';
import { HasAnyPermissionDirective } from '@src/app/directives/permission/has-any-permission.directive';
import { HasEveryPermissionDirective } from '@src/app/directives/permission/has-every-permission.directive';
import { NgSelectModule } from '@ng-select/ng-select';
import { NotificationModule } from '@src/app/core/notification/notification.module';
import { RequestHeaderInterceptor } from '@src/app/core/interceptors/request-header.interceptor';
import { RequestMockInterceptor } from '@src/app/core/interceptors/request-mock.interceptor';
import { DeleteModalComponent } from '@src/app/shared/modals/delete-modal/delete-modal.component';
import { CloseModalComponent } from '@src/app/shared/modals/close-modal/close-modal.component';
import { ConfirmModalComponent } from '@src/app/shared/modals/confirm-modal/confirm-modal.component';
import { RequestDateConvertInterceptor } from '@src/app/core/interceptors/request-date-convert.interceptor';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@NgModule( {
    declarations: [
        FormatDateModelDirective,
        NumberCheckerDirective,
        SetFocusDirective,
        PageNotFoundComponent,

        HasAnyPermissionDirective,
        HasEveryPermissionDirective,

        DeleteModalComponent,
        CloseModalComponent,
        ConfirmModalComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,

        DpDatePickerModule,
        NgSelectModule,

        LayoutSharedModule,
        TableControlModule,
        MaterialSharedModule,
        PrimeNgSharedModule,
        BootstrapSharedModule,
        NotificationModule,

    ],
    exports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,

        PageNotFoundComponent,

        HasAnyPermissionDirective,
        HasEveryPermissionDirective,

        DpDatePickerModule,
        NgSelectModule,

        LayoutSharedModule,
        TableControlModule,
        MaterialSharedModule,
        PrimeNgSharedModule,
        BootstrapSharedModule,
        NotificationModule,

        FormatDateModelDirective,
        NumberCheckerDirective,
        SetFocusDirective,

        TranslateModule,

        DeleteModalComponent,
        CloseModalComponent,
        ConfirmModalComponent,
    ],
    entryComponents: [
        PageNotFoundComponent,
        DeleteModalComponent,
        CloseModalComponent,
        ConfirmModalComponent,
    ],
    providers: [
        AuthGuard,
        PermissionGuard,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: RequestMockInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: RequestHeaderInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: RequestDateConvertInterceptor,
            multi: true
        },

        {
            provide: LocationStrategy,
            useClass: HashLocationStrategy
        }
    ],
    bootstrap: []
} )
export class CoreSharedModule {

}
