import { Component, EventEmitter, Output } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap';
import { BaseComponent } from '@src/app/shared/components/base/base-component';

@Component( {
  selector: 'peas-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrls: [ './confirm-modal.component.scss' ]
} )
export class ConfirmModalComponent extends BaseComponent {

  @Output() confirm: EventEmitter<boolean> = new EventEmitter();

  public title: string;
  public text: string;

  constructor( public modal: BsModalRef ) {
    super();
  }

  onConfirm() {
    this.confirm.emit( true );
    this.modal.hide();
  }

}

