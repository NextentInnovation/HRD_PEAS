import { Directive, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { PermissionUtils } from '../../core/utils/permission.util';

@Directive( {
  selector: '[peasHasAnyPermission]'
} )
export class HasAnyPermissionDirective implements OnInit {

  @Input( 'peasHasAnyPermission' ) permissions;

  constructor( private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef ) {
  }

  ngOnInit() {
    console.log('Any? : ', PermissionUtils.hasAnyPermission( this.permissions ));
    if ( PermissionUtils.hasAnyPermission( this.permissions ) ) {
      // If condition is true add template to DOM
      this.viewContainer.createEmbeddedView( this.templateRef );
    } else {
      // Else remove template from DOM
      this.viewContainer.clear();
    }

  }

}
