export class PageSizerConfig {

  static DEFAULT_PAGE_SIZE = 10;
  static PAGE_SIZES: number[] = [ 2, 5, 10, 20, 50, 100 ];

  totalItem: number;
  defaultPageSize: number;
  allEnable = true;

  constructor( defaultPageSize?: number, totalItem?: number, allEnable?: boolean ) {

    this.totalItem = totalItem || null;
    this.defaultPageSize = defaultPageSize || PageSizerConfig.DEFAULT_PAGE_SIZE;
    this.allEnable = allEnable || false;

  }

}

