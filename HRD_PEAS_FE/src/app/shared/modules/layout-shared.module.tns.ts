import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@NgModule( {
  declarations: [
  ],

  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
  ],
  bootstrap: [],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
} )
export class LayoutSharedModule {
}
