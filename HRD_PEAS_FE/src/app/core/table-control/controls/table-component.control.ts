import { SortDirection } from '../constants/sort-direction.contant';
import { TableStorageControl } from './table-storage.control';
import { CommonUtils } from '../../utils/common.util';
import { Subject } from 'rxjs';
import { TableLocalSorterControl } from './table-local-sorter.control';
import { TableOptions } from '../models/table-options.model';
import { Pagination } from '../models/pagination.model';

export class TableComponentControl {

  private pagination = new Subject<Pagination>();
  private localSorter: TableLocalSorterControl = new TableLocalSorterControl();

  public sortDirection: SortDirection;

  public onChange = this.pagination.asObservable();

  constructor( public options: TableOptions ) {
    TableStorageControl.setTable( this.options.name, this.options.paginationConfig.itemsPerPage, this.options.sortDirection, this.options.sortedColumn );
    TableStorageControl.setPageSize( this.options.name, this.options.paginationConfig.itemsPerPage );
    TableStorageControl.setSortDirection( this.options.name, this.options.sortDirection );
    TableStorageControl.setSortedColumn( this.options.name, this.options.sortedColumn );

    this.setColumnSortDirection( this.options.sortedColumn, this.options.sortDirection );

  }

  setPage( page ) {
    if ( CommonUtils.isNotEmpty( page ) ) {
      this.options.paginationConfig.currentPage = Number( page );
      TableStorageControl.setCurrentPage( this.options.name, page );
    }
  }

  setPageSize( size: number ) {
    this.options.paginationConfig.itemsPerPage = size;
    this.options.pageSize = size;
    TableStorageControl.setPageSize( this.options.name, size );
    if ( this.options.isAsync ) {
      this.pagination.next( this.options.getPagination() );
    }

  }

  setColumnSortDirection( column: string, type: SortDirection ) {
    this.setSortDirection( type );
    this.setSortColumn( column );
  }

  setSortDirection( sortDirection: SortDirection ) {
    if ( sortDirection ) {
      this.sortDirection = sortDirection;
      TableStorageControl.setSortDirection( this.options.name, sortDirection );
    }
  }

  setSortColumn( sortedColumn: string ) {
    if ( sortedColumn ) {
      this.options.sortedColumn = sortedColumn;
      TableStorageControl.setSortedColumn( this.options.name, sortedColumn );
    }
  }

  initSortingOrder( columnName: string ) {
    if ( this.isSortedColumn( columnName ) ) {
      this.reverseSortOrder();
    } else {
      this.setColumnSortDirection( columnName, SortDirection.ASC );
    }
  }

  reverseSortOrder() {
    // this.options.sortDirection = this.options.sortDirection === SortDirection.ASC ? SortDirection.DESC : SortDirection.ASC;
    this.sortDirection = this.sortDirection === SortDirection.ASC ? SortDirection.DESC : SortDirection.ASC;
  }


  isSortedColumn( columnName: string ) {
    return columnName.localeCompare( this.options.sortedColumn ) === 0;
  }


  // TODO: refact
  sort( obj: any, sortname: string ) {
    if ( obj != null ) {
      this.setPage( 1 );
      this.initSortingOrder( sortname );
      if ( this.options.isAsync && !this.onlyOnePage() ) {
        this.pagination.next( this.options.getPagination() );
      } else {
        if ( this.options.router != null ) {
          this.options.router.navigate(
            [ CommonUtils.getBaseUrl( this.options.router.url ) ],
            {
              queryParamsHandling: 'merge',
              queryParams: {
                sort_col: sortname,
                sort_order: this.options.sortDirection,
                page: this.options.paginationConfig.currentPage
              }
            }
          );
        }


        this.localSorter.sorting( obj, sortname );
      }
    }
  }

  onlyOnePage(): boolean {
    return (this.options.paginationConfig.totalItems - this.options.paginationConfig.itemsPerPage) <= 0;
  }

  onPageChange( page ) {
    this.setPage( page );
    if ( this.options.isAsync ) {
      this.pagination.next( this.options.getPagination() );
    }
  }


}
