import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap';
import { BaseComponent } from '@src/app/shared/components/base/base-component';

@Component({
  selector: 'peas-close-modal',
  templateUrl: './close-modal.component.html',
  styleUrls: ['./close-modal.component.scss']
})
export class CloseModalComponent extends BaseComponent {

  @Output() close: EventEmitter<string> = new EventEmitter();

  public title: string;
  public validTo: string;

  constructor( public modal: BsModalRef ) {
    super();
  }

  onClose() {
    this.close.emit( this.validTo );
    this.modal.hide();
  }

}
