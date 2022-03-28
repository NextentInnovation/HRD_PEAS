import { Component, EventEmitter, Output } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap';
import { BaseComponent } from '@src/app/shared/components/base/base-component';

@Component( {
  selector: 'peas-delete-modal',
  templateUrl: './delete-modal.component.html',
  styleUrls: [ './delete-modal.component.scss' ]
} )
export class DeleteModalComponent extends BaseComponent {

  @Output() delete: EventEmitter<boolean> = new EventEmitter();

  constructor( public modal: BsModalRef ) {
    super();
  }

  onDelete() {
    this.delete.emit( true );
    this.modal.hide();
  }

}
