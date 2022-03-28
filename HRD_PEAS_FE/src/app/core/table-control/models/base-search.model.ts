import { Pagination } from '@src/app/core/table-control/models/pagination.model';
/**
 *  Use this base class to extends custom search object in table component
 */

export class BaseSearch {

  number: number;
  order: string;
  size: number;
  sort: string;

  static searchPagerMarge(search: BaseSearch, pagination?: Pagination): BaseSearch {
    if (pagination) {
      search.number = pagination.number;
      search.size = pagination.size;
      search.sort = pagination.sort;
      search.order = pagination.sortDirection;
    }
    return search;
  }

  /**
   * @deprecated Use other method: SearchClearUtil.clear(searchObj: object);
   */
  clear() {
    Object.keys( this ).forEach( key => {
      this[ key ] = null;
    } );
  }
}
