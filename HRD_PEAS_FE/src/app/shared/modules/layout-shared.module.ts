import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardComponent } from '@src/app/core/layouts/web/card/card.component';

@NgModule( {
  declarations: [
    CardComponent,
  ],

  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
      CardComponent,
  ],
  bootstrap: [],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
} )
export class LayoutSharedModule {
}
