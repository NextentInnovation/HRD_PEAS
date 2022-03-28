import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { Permissions } from '@src/app/core/constants/permissions.constants';
import { Router } from '@angular/router';

@Component({
  selector: 'peas-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent extends BaseComponent implements OnInit {
  Permissions = Permissions;
  @Output() closeMenu = new EventEmitter<any>();

  constructor(private router: Router) {
    super();
  }

  ngOnInit() {
  }

  menuClicked() {
    this.closeMenu.emit();
  }

  isThisUrl(watchUrl: string): boolean {
    return this.router.url.toLocaleLowerCase().includes(watchUrl.toLocaleLowerCase());
  }

}
