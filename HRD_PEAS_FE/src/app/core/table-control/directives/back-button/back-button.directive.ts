import { Directive, HostListener } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { CommonUtils } from '../../../utils/common.util';

@Directive({
  selector: '[peasBackButton]'
})
export class BackButtonDirective {

  constructor(private location: Location, private router: Router) { }

  @HostListener('click')
  onClick() {
    if (CommonUtils.isNotEmpty(this.router.routerState.snapshot.root.queryParamMap.get('table'))) {
      window.history.go( -2);
    } else {
      this.location.back();
    }
  }

}
