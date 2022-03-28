import { NgModule } from '@angular/core';

import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { InputSwitchModule } from 'primeng/inputswitch';
import { SelectButtonModule } from 'primeng/selectbutton';
import { CheckboxModule } from 'primeng/checkbox';
import { ToastModule } from 'primeng/toast';
import { CalendarModule } from 'primeng/calendar';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ListboxModule } from 'primeng/listbox';


@NgModule( {
    declarations: [],
    imports: [
        InputTextModule,
        DropdownModule,
        MultiSelectModule,
        InputSwitchModule,
        SelectButtonModule,
        CheckboxModule,
        ToastModule,
        CalendarModule,
        InputTextareaModule,
        ListboxModule
    ],
    exports: [
        InputTextModule,
        DropdownModule,
        MultiSelectModule,
        InputSwitchModule,
        SelectButtonModule,
        CheckboxModule,
        ToastModule,
        CalendarModule,
        InputTextareaModule,
        ListboxModule
    ],
} )
export class PrimeNgSharedModule {
}
