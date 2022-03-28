import { Routes } from '@angular/router';
import { PageNotFoundComponent } from '@src/app/core/layouts/page-not-found/page-not-found.component';
import { EvaluationComponent } from '@src/app/modules/rating/evaluation/evaluation.component';
import { SiteURLS } from '@src/app/core/constants/urls/site-url.constant';
import { RatingComponent } from '@src/app/modules/rating/rating/rating.component';

export const routes: Routes = [
    {
        path: SiteURLS.RATING.EVALUATION + '/:id',
        component: EvaluationComponent,
    },
    {
        path: SiteURLS.RATING.RATING + '/:id',
        component: RatingComponent,
    },
    {
        path: '**',
        component: PageNotFoundComponent,
    }
];
