import { NgModule } from '@angular/core';
import { routes } from '@src/app/modules/rating/rating.routes';
import { NativeScriptRouterModule } from 'nativescript-angular';

@NgModule({
  imports: [NativeScriptRouterModule.forChild(routes)],
  exports: [NativeScriptRouterModule]
})
export class RatingRoutingModule { }
