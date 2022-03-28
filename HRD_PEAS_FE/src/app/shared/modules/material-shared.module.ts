import { NgModule } from '@angular/core';
import {
    MatCheckboxModule,
    MatIconModule, MatInputModule,
    MatSidenavModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatToolbarModule
} from '@angular/material';
import { MatButtonModule } from '@angular/material/button';

@NgModule( {
    declarations: [],
    imports: [
        MatSnackBarModule,
        MatSlideToggleModule,
        MatIconModule,
        MatSidenavModule,
        MatSnackBarModule,
        MatCheckboxModule,
        MatToolbarModule,
        MatInputModule,
        MatButtonModule
    ],
    exports: [
        MatSnackBarModule,
        MatSlideToggleModule,
        MatIconModule,
        MatSidenavModule,
        MatSnackBarModule,
        MatCheckboxModule,
        MatToolbarModule,
        MatInputModule,
        MatButtonModule
    ],
} )
export class MaterialSharedModule {
}
