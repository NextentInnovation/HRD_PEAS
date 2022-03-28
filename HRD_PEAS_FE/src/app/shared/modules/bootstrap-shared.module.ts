import { NgModule } from '@angular/core';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';


@NgModule( {
    declarations: [],
    imports: [
        BsDropdownModule.forRoot(),
        TooltipModule.forRoot(),
        ModalModule.forRoot(),
    ],
    exports: [
        BsDropdownModule,
        TooltipModule,
    ],
} )
export class BootstrapSharedModule {
}
