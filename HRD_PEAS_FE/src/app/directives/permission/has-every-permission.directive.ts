import { Directive, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { PermissionUtils } from '../../core/utils/permission.util';

@Directive( {
  selector: '[peasHasEveryPermission]'
} )
export class HasEveryPermissionDirective implements OnInit {

  @Input( 'peasHasEveryPermission' ) permissions;

  constructor( private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef ) {
  }

  ngOnInit() {
    if ( PermissionUtils.hasEveryPermission( this.permissions ) ) {
      // If condition is true add template to DOM
      this.viewContainer.createEmbeddedView( this.templateRef );
    } else {
      // Else remove template from DOM
      this.viewContainer.clear();
    }
  }

}
