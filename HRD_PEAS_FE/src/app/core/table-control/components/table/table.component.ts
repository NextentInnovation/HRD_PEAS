import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TableOptions } from '../../models/table-options.model';
import { TableComponentControl } from '../../controls/table-component.control';
import { PageSizerConfig } from '../../configs/page-sizer.config';
import { TableStorageControl } from '../../controls/table-storage.control';
import { CommonUtils } from '../../../utils/common.util';

@Component( {
  selector: 'peas-table',
  templateUrl: './table.component.html',
  styleUrls: [ './table.component.scss' ]
} )
export class TableComponent implements OnInit {

  @Input() options: TableOptions;
  @Input() list: Array<any>;

  @Output() pageChangeEvent = new EventEmitter();
  @Output() pageSizeChangeEvent = new EventEmitter();
  @Output() refresh: EventEmitter<TableOptions> = new EventEmitter<TableOptions>();

  public control: TableComponentControl;
  public pageSizerConfig: PageSizerConfig;

  constructor() {
  }

  ngOnInit() {

    // console.log( '[TableOptions]', this.options );

    this.control = new TableComponentControl( this.options );
    this.control.onChange.subscribe( pagination => {
      this.refresh.emit( this.options );
    } );

    /*
    this.options.router.events.subscribe(
      event => console.log(event)
    );
    */

    this.initPageSizerConfig();
    // this.initTableBeckUp();
  }

  public startSearch() {
    this.options.saveSearch();
    this.refresh.emit();
  }

  sorting() {
    if ( this.options.isAsync ) {
      this.refresh.emit( this.options );
    } else {
      this.control.sort( this.options.datas, this.options.sortedColumn );
    }
    this.options.initTableBeckUp();
  }


  changePageSize( $event ) {
    this.control.setPageSize( $event );
    this.options.initTableBeckUp();
    this.pageSizeChangeEvent.emit( $event );
  }

  pageChange( $event ) {
    this.control.onPageChange( $event );
    this.options.initTableBeckUp();
    this.pageChangeEvent.emit( $event );
  }


  getPages() {
    const numberOfPages = Math.ceil( this.options.paginationConfig.totalItems / this.options.paginationConfig.itemsPerPage );
    return Array.from( new Array( numberOfPages > 0 ? numberOfPages : 1 ), ( val, index ) => index + 1 );
  }


  initPageSizerConfig() {
    this.pageSizerConfig = new PageSizerConfig(
      this.options.paginationConfig.itemsPerPage,
      this.options.paginationConfig.totalItems,
      false
    );
  }

}
