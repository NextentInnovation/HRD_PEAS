import { AfterViewInit, Directive, ElementRef, Input } from '@angular/core';
import { CommonUtils } from '../../core/utils/common.util';

@Directive( {
  selector: '[sasSetFocusAfterLoad]'
} )
export class SetFocusDirective implements AfterViewInit {
  @Input('sasSetFocusAfterLoad') isFocus = true;

  constructor(private hostElement: ElementRef) {
  }

  ngAfterViewInit() {
    if (CommonUtils.isEmpty(this.isFocus) ) {
      this.isFocus = true;
    }
    console.log('init: ', this.isFocus);
    if (this.isFocus) {
      // Modal miatt szükséges késleltetés. mivel a modal kinyitása után közvetlenül, még nem lesz sikeres a focus parancs
      setTimeout(fcall => {
        this.hostElement.nativeElement.focus();
      }, 100);
      console.log('focus');
    }
  }
}
