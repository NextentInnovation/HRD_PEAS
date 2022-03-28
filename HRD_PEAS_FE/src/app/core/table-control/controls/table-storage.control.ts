import { TableStorageKey } from '../constants/table-storage-key.constant';
import { SortDirection } from '../constants/sort-direction.contant';
import { CommonUtils } from '../../utils/common.util';

export class TableStorageControl {

  static setFullTableStore;

  TableStorageKey = TableStorageKey;

  static clear( tableName: string ) {
    TableStorageControl.clearWithoutShort( tableName );
    localStorage.removeItem( TableStorageKey.SORTED_COLUMN.replace( '{table}', tableName ) );
    localStorage.removeItem( TableStorageKey.SORT_DIRECTION.replace( '{table}', tableName ) );
  }

  static clearWithoutShort( tableName: string ) {
    localStorage.removeItem( TableStorageKey.TABLE_NAME.replace( '{table}', tableName ) );
    localStorage.removeItem( TableStorageKey.CURRENT_PAGE.replace( '{table}', tableName ) );
    localStorage.removeItem( TableStorageKey.SEARCH_OBJECT.replace( '{table}', tableName ) );
  }

  static setTable( tableName: string, pageSize, sortedColumn, sortDirection  ) {
  // TODO:
    localStorage.setItem( '[TABLE::' + tableName + ']', TableStorageKey.FULL_TABLE_STORE.replace('{pageSize}', pageSize )
                                                                       .replace('{sortedColumn}', sortedColumn )
                                                                       .replace('{sortDirection}', sortDirection ) );
  }

  static setPageSize( tableName: string, pageSize: number ) {
    localStorage.setItem( TableStorageKey.DEFAULT_PAGE_SIZE.replace( '{table}', tableName ), pageSize.toString() );
  }

  static getPageSize( tableName: string ) {
    return Number( localStorage.getItem( TableStorageKey.DEFAULT_PAGE_SIZE.replace( '{table}', tableName ) ) );
  }

  static setSortDirection( tableName: string, direction: SortDirection ) {
    if ( CommonUtils.isNotEmpty( direction ) ) {
      localStorage.setItem( TableStorageKey.SORT_DIRECTION.replace( '{table}', tableName ), JSON.stringify(direction) );
    }
  }

  static getSortDirection( tableName: string ): SortDirection {
    const storage = localStorage.getItem( TableStorageKey.SORT_DIRECTION.replace( '{table}', tableName ) );
    let ret = null;
    if (CommonUtils.isNotEmpty(storage)) {
      ret = JSON.parse(storage);
    }
    return ret;
  }

  static setSortedColumn( tableName: string, column: string ) {
    if ( CommonUtils.isNotEmpty( column ) ) {
      localStorage.setItem( TableStorageKey.SORTED_COLUMN.replace( '{table}', tableName ), column );
    }
  }

  static getSortedColumn( tableName: string ): string {
    return localStorage.getItem( TableStorageKey.SORTED_COLUMN.replace( '{table}', tableName ) );
  }

  static setSearchObject( tableName: string, searchObj: object ) {
    if ( CommonUtils.isNotEmpty( searchObj ) ) {
      localStorage.setItem( TableStorageKey.SEARCH_OBJECT.replace( '{table}', tableName ), JSON.stringify(searchObj) );
    }
  }

  static getSearchObject( tableName: string ): object {
    return JSON.parse( localStorage.getItem( TableStorageKey.SEARCH_OBJECT.replace( '{table}', tableName ) ) );
  }

  static setCurrentPage( tableName: string, page: string ) {
    if ( CommonUtils.isNotEmpty( page ) ) {
      localStorage.setItem( TableStorageKey.CURRENT_PAGE.replace( '{table}', tableName ), page );
    }
  }

  static getCurrentPage( tableName: string ): number {
    let ret = 1;
    const currentNumber = localStorage.getItem( TableStorageKey.CURRENT_PAGE.replace( '{table}', tableName ) );
    if ( CommonUtils.isNotEmpty( localStorage.getItem( TableStorageKey.CURRENT_PAGE.replace( '{table}', tableName ) ) ) ) {
      ret = Number.parseInt( currentNumber, 10 );
    }
    return ret;
  }

}

