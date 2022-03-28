import { Router } from '@angular/router';
import { SortDirection } from '../constants/sort-direction.contant';
import { CommonUtils } from '../../utils/common.util';
import { TableStorageControl } from '../controls/table-storage.control';
import { Pagination } from './pagination.model';
import { PageSizerConfig } from '../configs/page-sizer.config';
import { PaginationInstance } from 'ngx-pagination';
import { TableSearchControl } from '../controls/table-search.control';
import { BaseSearch } from './base-search.model';

export class TableOptions {

  // ngx-pagination paginate pipe configuration
  public paginationConfig: PaginationInstance = {
    id: 'undefined',
    itemsPerPage: PageSizerConfig.DEFAULT_PAGE_SIZE,
    totalItems: 0,
    currentPage: 1
  };

  public sortedColumn: string;
  public sortDirection: SortDirection;
  public isPagination = true;

  public originalDatas: any = null;
  public datas: any = [];

  constructor(
    public name: string,
    public router: Router,
    public pageSize: number,
    public searchObject?: BaseSearch,
    public reLoad?: boolean, // Visszagomb esetén a táblázat visszatöltse-e az elözőleg használt szűrési feltételeket
    public isAsync?: boolean
  ) {
    this.paginationConfig.id = name;
    this.paginationConfig.itemsPerPage = TableStorageControl.getPageSize( name ) ? TableStorageControl.getPageSize( name ) : pageSize;
    this.isAsync = isAsync != null ? isAsync : true;
    this.reLoad = reLoad != null ? reLoad : false;
    if (CommonUtils.isNotEmpty(this.router.routerState.snapshot.root.queryParamMap.get('table'))) {
      this.loadTableControlDatas();
    } else {
      TableStorageControl.clear(this.name);
    }
  }

  loadTableControlDatas() {
    if (TableStorageControl.getSortDirection(this.name) ) {
      this.sortDirection = TableStorageControl.getSortDirection(this.name);
    }
    if (TableStorageControl.getSortedColumn(this.name) ) {
      this.sortedColumn = TableStorageControl.getSortedColumn(this.name);
    }
    if (TableStorageControl.getSearchObject(this.name) ) {
      this.searchObject = CommonUtils.setParamsWithOtherObject<BaseSearch>( this.searchObject,  TableStorageControl.getSearchObject( this.name ) );
    }
    if (TableStorageControl.getCurrentPage(this.name) ) {
      this.paginationConfig.currentPage = TableStorageControl.getCurrentPage(this.name);
    }
  }

  // For webservices pagination configuration
  getPagination() {
    return new Pagination(
      this.paginationConfig.currentPage - 1,
      this.paginationConfig.itemsPerPage,
      this.sortedColumn,
      this.sortDirection
    );
  }

  update( totalElements, list ) {
    if ( list && !this.isAsync && this.searchObject && !this.originalDatas ) {
      this.originalDatas = JSON.parse( JSON.stringify( list ) );
    }
    if ( list && !this.isAsync && this.searchObject ) {
      list = TableSearchControl.searchWidthObj( this.originalDatas, this.searchObject );
      this.paginationConfig.totalItems = list.length;
    } else {
      this.paginationConfig.totalItems = totalElements;
    }
    if ( CommonUtils.isNotEmpty( list ) ) {
      this.datas = list;
    }
    return list;
  }

  // on version change the source list doest refresh
  setSourceList( sourceList ) {
    this.originalDatas = Array.from( sourceList );
  }

  public initTableBeckUp() {
    console.log('init preload?: ', this.reLoad);
    if ( this.reLoad && this.router != null ) {
      this.router.navigate( [ CommonUtils.getBaseUrl( this.router.url ) ], {
        queryParamsHandling: 'merge',
        queryParams: { table: 'preload' }
      } );
    }
  }

  public saveSearch() {
    TableStorageControl.setSearchObject( this.name, this.searchObject );
    this.initTableBeckUp();
  }

}
