import { Directive, forwardRef, HostListener, Inject } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';

/***
 * This directive use in inputs, and some action emmit the parent TableComponent refresh method
 */
@Directive( {
  selector: '[peasSearchColumn]'
} )
export class SearchColumnDirective {

  // Változást észlelő event, dátum választóval nem működik
  @HostListener( 'change', [ '$event' ] )
  onChange( event ) {
    console.log(event, typeof event);
    // Szűrés, hogy input text mezőkre ne reagáljon, így csak a select-re regál jelenleg
    if ( !(event && event.target && event.target.type === 'text') ) {
      this.parent.startSearch();
    }
  }

  // Dátumválasztó - dátumra kattintás event
  @HostListener( 'onSelect', [ '$event' ] )
  onSelect( event ) {
    // only use for <select>
    console.log(event);
    this.parent.startSearch();
  }

  // Dátumválasztó - dátum törlése event
  @HostListener( 'onClearClick', [ '$event' ] )
  onClearClick( event ) {
    // only use for <select>
    console.log(event);
    this.parent.startSearch();
  }

  // Az enter lenyomására lefuttat egy keresést
  @HostListener( 'keydown.enter', [ '$event' ] )
  keyEvent( event: KeyboardEvent ) {
    this.parent.startSearch();
  }

  constructor( @Inject( forwardRef( () => TableComponent ) ) private parent: TableComponent ) {
  }

}
