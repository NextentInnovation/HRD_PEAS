export class Pagination {

  sort: string;
  sortDirection: string;
  number: number; // page-ről átnevezve az új BE-hez, de meghagyva a régi hívást a régebbi methódusok miatt
  size: number;

  constructor( page, size, sort, sortDirection ) {
    this.sort = sort;
    this.sortDirection = sortDirection;
    this.size = size;
    this.number = page;
  }

  get page(): number {
    return this.number;
  }

  set page(value: number) {
    this.number = value;
  }
}
