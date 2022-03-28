import { Component, ElementRef, Input, OnInit, TemplateRef, ViewChild } from '@angular/core';

@Component({
  selector: 'peas-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent implements OnInit {

  // Card title
  @Input( 'title' ) title: string;
  @Input( 'titleTemplate' ) titleTemplate: TemplateRef<any>;

  @ViewChild( 'card', {static: false} ) card: ElementRef;

  constructor() {

  }

  ngOnInit() {
  }

}
