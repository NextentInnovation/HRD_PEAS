import { NgModule } from '@angular/core';
import { SortableColumnComponent } from './components/sortable-column/sortable-column.component';
import { SearchColumnDirective } from './directives/search-column/search-column.directive';
import { ColumnComponent } from './components/column/column.component';
import { TableComponent } from './components/table/table.component';
import { PageSizeComponent } from './components/page-size/page-size.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SearchButtonComponent } from './components/search-button/search-button.component';
import { TranslateModule } from '@ngx-translate/core';
import { BackButtonDirective } from './directives/back-button/back-button.directive';

@NgModule( {
    declarations: [
        TableComponent,
        ColumnComponent,
        PageSizeComponent,
        SortableColumnComponent,

        SearchColumnDirective,
        SearchButtonComponent,
        BackButtonDirective,
    ],
    imports: [
        CommonModule,
        FormsModule,
        NgxPaginationModule,
        TranslateModule
    ],
    exports: [
        NgxPaginationModule,
        TranslateModule,

        TableComponent,
        ColumnComponent,
        PageSizeComponent,
        SortableColumnComponent,

        SearchColumnDirective,
        BackButtonDirective,
        SearchButtonComponent,
    ],
} )
export class TableControlModule {
}
