import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { RatingSendModel } from '@src/app/models/rating/rating.model';

export class RatingServices extends BaseService {

    constructor(private http: HttpClient) {
        super();
    }

    set(rating: RatingSendModel, ratingId: string) {
        return this.http.post<RatingSendModel>( ApiURLS.RATING_SET.replace('{ratingId}', ratingId), rating );
    }
}
