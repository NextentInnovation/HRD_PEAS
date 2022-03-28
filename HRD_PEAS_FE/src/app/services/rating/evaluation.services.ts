import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { EvaluationModel } from '@src/app/models/rating/evaluation.model';

export class EvaluationServices extends BaseService {

    constructor(private http: HttpClient) {
        super();
    }

    get(evaluationId) {
        return this.http.get<EvaluationModel>( ApiURLS.EVALUATION_GET.replace('{evaluationId}', evaluationId) );
    }

    set(evaluation: EvaluationModel) {
        return this.http.put<EvaluationModel>( ApiURLS.EVALUATION_CREATE.replace('{evaluationId}', evaluation.id.toString()), evaluation );
    }
}
