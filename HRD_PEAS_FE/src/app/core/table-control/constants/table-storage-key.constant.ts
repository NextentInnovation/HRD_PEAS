export enum TableStorageKey {
  TABLE_NAME = '{table}_TABLE_NAME',
  DEFAULT_PAGE_SIZE = '{table}_DEFAULT_PAGE_SIZE',
  SORT_DIRECTION = '{table}_SORT_DIRECTION',
  SORTED_COLUMN = '{table}_SORTED_COLUMN',
  FULL_TABLE_STORE = '[pageSize::{pageSize}][sort::{sortedColumn}][sortDirection::{sortDirection}',
  SEARCH_OBJECT = '{table}_SEARCH_OBJECT',
  CURRENT_PAGE = '{table}_CURRENT_PAGE'
  // 'table::{table}pageSize::{pageSize};sort::{sortedColumn};sortDirection::{sortDirection}'
}
