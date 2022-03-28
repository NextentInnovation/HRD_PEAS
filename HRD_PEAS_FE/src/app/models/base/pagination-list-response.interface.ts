export class PaginationListResponse<T> {

  content: Array<T>;
  totalElements: number;
  totalPages: number;
  size: number;
  numberOfElements: number;
  empty: boolean;
  first: boolean;
  last: boolean;

}
