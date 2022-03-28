import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { NativeScriptModule } from 'nativescript-angular/nativescript.module';

import { AppRoutingModule } from '@src/app/app-routing.module';
import { AppComponent } from '@src/app/app.component';
import { TestComponent } from '@src/app/test/test.component';
import { BarcodeScanner } from 'nativescript-barcodescanner';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { CustomLoader } from '@src/app/core/services/translate-loader.service';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { MainWrapperComponent } from '@src/app/core/layouts/main-wrapper/main-wrapper.component';
import { HeaderComponent } from '@src/app/core/layouts/web/header/header.component';
import { MenuComponent } from '@src/app/core/layouts/web/menu/menu.component';
import { FooterComponent } from '@src/app/core/layouts/web/footer/footer.component';
import { NotificationModule } from '@src/app/core/notification/notification.module';

// Uncomment and add to NgModule imports if you need to use two-way binding
// import { NativeScriptFormsModule } from 'nativescript-angular/forms';

// Uncomment and add to NgModule imports  if you need to use the HTTP wrapper
// import { NativeScriptHttpClientModule } from 'nativescript-angular/http-client';

@NgModule( {
    declarations: [
        AppComponent,
        TestComponent,
        MainWrapperComponent,
        HeaderComponent,
        MenuComponent,
        FooterComponent,
    ],
    imports: [
        NativeScriptModule,
        AppRoutingModule,
        NotificationModule,
        
        TranslateModule.forRoot( {
            loader: {
                provide: TranslateLoader,
                useClass: CustomLoader,
            },
            isolate: false
        } ),
        CoreSharedModule,
        CoreSharedModule,
        CoreSharedModule,
    ],
    providers: [
        BarcodeScanner
    ],
    bootstrap: [ AppComponent ],
    schemas: [ NO_ERRORS_SCHEMA ]
} )
export class AppModule {
}
