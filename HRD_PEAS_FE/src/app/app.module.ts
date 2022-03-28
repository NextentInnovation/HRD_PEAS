import { NgModule } from '@angular/core';

import { AppRoutingModule } from '@src/app/app-routing.module';
import { AppComponent } from '@src/app/app.component';
import { TestComponent } from '@src/app/test/test.component';

import { InternationalNamedayService } from '@src/app/core/services/international-nameday.service';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { CustomLoader } from '@src/app/core/services/translate-loader.service';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { MainWrapperComponent } from '@src/app/core/layouts/main-wrapper/main-wrapper.component';
import { HeaderComponent } from '@src/app/core/layouts/web/header/header.component';
import { MenuComponent } from '@src/app/core/layouts/web/menu/menu.component';
import { FooterComponent } from '@src/app/core/layouts/web/footer/footer.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginService } from '@src/app/services/login/login.service';
import { NotificationIFService } from '@src/app/services/notification/notification.service';
import { ToDoService } from '@src/app/services/to-do/to-do.service';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}

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
        BrowserAnimationsModule,
        AppRoutingModule,
        CoreSharedModule,
        NgbModule,

        TranslateModule.forRoot( {
            loader: {
                provide: TranslateLoader,
                useClass: CustomLoader,
                // Lokális nyelvi fájl használata
                /*provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]*/
            },
            isolate: false
        } ),
    ],
    providers: [
        InternationalNamedayService,
        LoginService,
        NotificationIFService,
        ToDoService,
    ],
    bootstrap: [ AppComponent ]
} )
export class AppModule {
}
