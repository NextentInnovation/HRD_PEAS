import { Component, forwardRef, Inject, Input, OnInit } from '@angular/core';
import { TableComponent } from '../table/table.component';
import { BaseSearch } from '../../models/base-search.model';
import { SearchClearUtil } from '../../utils/search-clear.util';
import { TableStorageControl } from '../../controls/table-storage.control';

@Component({
  selector: 'sas-search-button',
  templateUrl: './search-button.component.html',
  styleUrls: ['./search-button.component.scss']
})
export class SearchButtonComponent implements OnInit {

  @Input() searchObject: BaseSearch;

  constructor( @Inject( forwardRef( () => TableComponent ) ) private parent: TableComponent ) {
  }

  ngOnInit() {
    if ( this.searchObject == null ) {
      console.error( '[SearchButtonComponent] missing parameter! (searchObject)' );
    }
  }

  search() {
    this.parent.startSearch();
  }

  clear() {
    SearchClearUtil.clear(this.searchObject);
    TableStorageControl.clear(this.parent.options.name);
    this.parent.options.paginationConfig.currentPage = 1;
    this.search();
  }

}
