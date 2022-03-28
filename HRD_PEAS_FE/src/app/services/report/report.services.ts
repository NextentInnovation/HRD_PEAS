import { SimplePeriodModel } from '@src/app/models/base/base-datas.model';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { EmployeeReportModel } from '@src/app/models/report/employee.report.model';
import {
    EmployeesPaginationReportModel,
    EmployeesReportModel,
    EmployeesReportSearchModel
} from '@src/app/models/report/employees.report.model';
import { BaseService } from '@src/app/core/services/base.service';
import { HttpClient } from '@angular/common/http';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { EvaluationReportModel } from '@src/app/models/report/evaluation.report.model';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { BaseSearch } from '@src/app/core/table-control/models/base-search.model';
import { finalize } from 'rxjs/operators';

export class ReportServices extends BaseService {

    constructor(private http: HttpClient) {
        super();
    }

    getPeriods() {
        return this.http.get<SimplePeriodModel[]>( ApiURLS.REPORT_PERIODS );
    }

    getEmployeeReport(ratingId?, periodId?, userId?) {
        let serach;
        if (CommonUtils.isNotEmpty(ratingId)) {
            serach = { ratingId: ratingId };
        } else {
            serach = { periodId: periodId, userId: userId };
        }
        return this.http.put<EmployeeReportModel>( ApiURLS.REPORT_EMPLOYEE, serach );
    }

    getEmployeesReport(search: EmployeesReportSearchModel, tableOptions: TableOptions) {
        this.addLoadingIndicator( tableOptions.name );
        return this.http.put<EmployeesPaginationReportModel>( ApiURLS.REPORT_EMPLOYEES, BaseSearch.searchPagerMarge(search, tableOptions.getPagination()) )
                   .pipe(finalize( () => this.removeLoadingIndicator( tableOptions.name ) ) );
    }

    getEvaluationReport(taskId: string) {
        return this.http.put<EvaluationReportModel>( ApiURLS.REPORT_EVALUATION.replace('{taskId}', taskId), null );
    }
}
