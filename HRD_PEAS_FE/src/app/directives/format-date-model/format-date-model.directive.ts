import { Directive, HostListener } from '@angular/core';
import { NgControl } from '@angular/forms';
import * as moment from 'moment';

@Directive( {
  selector: '[sasFormatDateModelDirective]'
} )
export class FormatDateModelDirective {

  constructor( private ngControl: NgControl ) {
  }

  @HostListener( 'keydown.enter' )
  enter() {
    if ( this.ngControl.control.value ) {
      this.ngControl.control.setValue( moment( this.ngControl.control.value, 'YYYYMMDD' ).format( 'YYYY-MM-DD' ), { emitEvent: true } );
    }
  }

}
