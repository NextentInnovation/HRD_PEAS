import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';

export class MockServiceList {
    public static serviceList = [
        { url: ApiURLS.LOGIN, method: 'POST', mockFile: './mock/login/login.txt', mock: false },
        { url: ApiURLS.LABEL, method: 'GET', mockFile: './assets/i18n/hu.json', mock: false },
        { url: ApiURLS.ME_USER_DATA, method: 'GET', mockFile: './mock/user/me.json', mock: false },
        { url: ApiURLS.BASE_DATAS, method: 'GET', mockFile: './mock/task/base-data.json', mock: false },
        { url: ApiURLS.TASK_GET.replace('{taskId}', ''), method: 'GET', mockFile: './mock/task/task.json', mock: false },
        { url: ApiURLS.TASK_LIST, method: 'GET', mockFile: './mock/task/task-list.json', mock: false },
        { url: ApiURLS.EVALUATION_GET.replace('{evaluationId}', ''), method: 'GET', mockFile: './mock/rating/evaluation.json', mock: false },
        { url: ApiURLS.REPORT_EMPLOYEE, method: 'PUT', mockFile: './mock/report/employee.json', mock: false },
        { url: ApiURLS.REPORT_EVALUATION.replace('{taskId}', ''), method: 'PUT', mockFile: './mock/report/evaluation.json', mock: false },
        { url: ApiURLS.REPORT_EMPLOYEES, method: 'PUT', mockFile: './mock/report/employees.json', mock: false },
        { url: ApiURLS.REPORT_PERIODS, method: 'GET', mockFile: './mock/report/periods.json', mock: false }
    ];
}
