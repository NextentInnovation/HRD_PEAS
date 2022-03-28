import { environment } from '../../../../environments/environment';

export class ApiURLS {

  private static BASE_PATH: string = environment.API_HOST + '';


  private static AUTH_MODULE: string = ApiURLS.BASE_PATH + '/peas-web';

  static LABEL: string = ApiURLS.AUTH_MODULE + '/label_v1';
  static AUTO_COMPLET: string = ApiURLS.AUTH_MODULE + '/autocomplet_v1';

  static LOGIN: string = ApiURLS.AUTH_MODULE + '/login_v1';
  static LOGOUT: string = ApiURLS.AUTH_MODULE + '/logout_v1';
  static ME_USER_DATA: string = ApiURLS.AUTH_MODULE + '/me_v1';

  static BASE_DATAS: string = ApiURLS.AUTH_MODULE + '/info_v1';

  private static TASK_MODULE: string = ApiURLS.BASE_PATH + '/peas-web/task_v1';

  static TASK_LIST: string = ApiURLS.TASK_MODULE;
  static TASK_CREATE: string = ApiURLS.TASK_MODULE;
  static TASK_EDIT: string = ApiURLS.TASK_MODULE + '/{taskId}';
  static TASK_GET: string = ApiURLS.TASK_MODULE + '/{taskId}';
  static TASK_DELETE: string = ApiURLS.TASK_MODULE + '/{taskId}';
  static TASK_COPY: string = ApiURLS.TASK_MODULE + '/copy_v1/{taskId}';
  static TASK_START_EVAULATION: string = ApiURLS.TASK_MODULE + '/start_evaluation/{taskId}';

  private static EVALUATION_MODULE: string = ApiURLS.BASE_PATH + '/peas-web/evaluation_v1';
  static EVALUATION_GET: string = ApiURLS.EVALUATION_MODULE + '/{evaluationId}';
  static EVALUATION_CREATE: string = ApiURLS.EVALUATION_MODULE + '/{evaluationId}';

  private static RATEING_MODULE: string = ApiURLS.BASE_PATH + '/peas-web/rating_v1';
  static RATING_SET: string = ApiURLS.RATEING_MODULE + '/{ratingId}';

  private static TO_DO_MODULE: string = ApiURLS.BASE_PATH + '/peas-web/todo_v1';
  static TO_DO_LIST: string = ApiURLS.TO_DO_MODULE + '/';

  private static NOTIFICATION_MODULE: string = ApiURLS.BASE_PATH + '/peas-web/notification_v1';
  static NOTIFICATION_LIST: string = ApiURLS.NOTIFICATION_MODULE + '/';

  private static REPORT_MODULE: string = ApiURLS.BASE_PATH + '/peas-web/report_v1';
  static REPORT_EMPLOYEE: string = ApiURLS.REPORT_MODULE + '/employee_v1';
  static REPORT_EMPLOYEES: string = ApiURLS.REPORT_MODULE + '/employees_v1';
  static REPORT_EVALUATION: string = ApiURLS.REPORT_MODULE + '/evaluation_v1/{taskId}';
  static REPORT_PERIODS: string = ApiURLS.REPORT_MODULE + '/period_v1';

  private static PERIOD_MODULE: string = ApiURLS.BASE_PATH + '/peas-web/period_v1';
  static PERIOD_LIST: string = ApiURLS.PERIOD_MODULE;
  static PERIOD_CREATE: string = ApiURLS.PERIOD_MODULE;
  static PERIOD_GET: string = ApiURLS.PERIOD_MODULE + '/{periodId}';
  static PERIOD_EDIT: string = ApiURLS.PERIOD_MODULE + '/{periodId}';
  static PERIOD_DELETE: string = ApiURLS.PERIOD_MODULE + '/{periodId}';
  static PERIOD_GET_GENERATE: string = ApiURLS.PERIOD_MODULE + '/next';


}
