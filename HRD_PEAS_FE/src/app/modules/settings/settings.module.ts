import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SettingsRoutingModule } from '@src/app/modules/settings/settings-routing.module';
import { CoreSharedModule } from '@src/app/shared/modules/core-shared.module';
import { PeriodListComponent } from '@src/app/modules/settings/period-list/period-list.component';
import { PeriodEditComponent } from '@src/app/modules/settings/period-edit/period-edit.component';
import { PeriodService } from '@src/app/services/settings/period.service';

@NgModule({
  declarations: [PeriodListComponent, PeriodEditComponent],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    CoreSharedModule,
  ],
  providers: [
    PeriodService
  ]
})
export class SettingsModule { }
