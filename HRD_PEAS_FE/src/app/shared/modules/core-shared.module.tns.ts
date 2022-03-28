import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { LayoutSharedModule } from './layout-shared.module';
import { FormatDateModelDirective } from '../../directives/format-date-model/format-date-model.directive';
import { NumberCheckerDirective } from '../../directives/number-checker/number-checker.directive';
import { TranslateModule } from '@ngx-translate/core';
import { AuthGuard } from '../../core/guards/auth.guard';
import { PermissionGuard } from '../../core/guards/permission.guard';
import { NativeScriptFormsModule } from 'nativescript-angular';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@NgModule( {
    declarations: [
        FormatDateModelDirective,
        NumberCheckerDirective,
        PageNotFoundComponent
    ],
    imports: [
        HttpClientModule,
        NativeScriptFormsModule,

        LayoutSharedModule,
    ],
    exports: [
        NativeScriptFormsModule,
        HttpClientModule,

        LayoutSharedModule,

        FormatDateModelDirective,
        NumberCheckerDirective,

        PageNotFoundComponent,

        TranslateModule,
    ],
    entryComponents: [],
    providers: [
        AuthGuard,
        PermissionGuard,
        DataTransferService,
        {
            provide: LocationStrategy,
            useClass: HashLocationStrategy
        }
    ],
    bootstrap: []
} )
export class CoreSharedModule {

}
