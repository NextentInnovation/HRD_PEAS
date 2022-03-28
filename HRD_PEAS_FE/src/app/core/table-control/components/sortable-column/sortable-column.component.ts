import { Component, ElementRef, forwardRef, HostListener, Inject, Input } from '@angular/core';
import { TableComponent } from '../table/table.component';
import { SortDirection } from '../../constants/sort-direction.contant';
import { TableStorageControl } from '../../controls/table-storage.control';

@Component( {
  selector: '[peas-sortable-column]',
  templateUrl: './sortable-column.component.html',
  styleUrls: [ './sortable-column.component.scss' ]
} )
export class SortableColumnComponent {

  SortDirection = SortDirection;

  @Input( 'peas-sortable-column' ) column = '';
  @Input() sortDirection: SortDirection;

  @HostListener( 'click' )
  sort() {

    this.sortDirection = this.sortDirection === SortDirection.ASC ? SortDirection.DESC : SortDirection.ASC;
    // this.table.control.reverseSortOrder();

    this.table.options.sortedColumn = this.column;
    this.table.options.sortDirection = this.sortDirection;

    TableStorageControl.setSortDirection( this.table.options.name, this.sortDirection );
    TableStorageControl.setSortedColumn( this.table.options.name, this.column );

    this.table.sorting();

  }

  constructor( element: ElementRef,
               @Inject( forwardRef( () => TableComponent ) ) public table: TableComponent ) {
    element.nativeElement.style.cursor = 'pointer';
  }

}
