import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PageSizerConfig } from '../../configs/page-sizer.config';

@Component({
  selector: 'sas-page-size',
  templateUrl: './page-size.component.html',
  styleUrls: ['./page-size.component.scss']
})
export class PageSizeComponent implements OnInit {

  @Input() totalItem: number;
  @Input() defaultPageSize: number = PageSizerConfig.DEFAULT_PAGE_SIZE;
  @Input() allEnable = false;

  @Input() config: PageSizerConfig;
  @Output() changePageSize = new EventEmitter();

  public sizes: number[] = PageSizerConfig.PAGE_SIZES;

  constructor() {
    if ( this.config != null) {
      // this.totalItem = this.config.totalItem || this.totalItem;
      this.defaultPageSize = this.config.defaultPageSize || this.defaultPageSize;
      this.allEnable = this.config.allEnable;
    }
  }

  ngOnInit() {
    // console.log('[ngOnInit] PageSizeSetterComponent');
    // console.log('[ngOnInit] defaultPageSize: ', this.defaultPageSize);
  }

  onChange( selected ) {
    this.changePageSize.emit( selected );
  }

}
