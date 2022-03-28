export class SiteURLS {
    public static PREFIX = '/';

    // Module urls
    public static BASE = {
        TEST_COMPONENTS: 'test',
    };

    public static LOGIN = {
        BASE: 'login'
    };

    public static NOTIFICATION = {
        BASE: 'notifications'
    };

    public static REPORT = {
        BASE: 'reports',
        EMPLOYEES: 'employees',
        EVALUATION: 'evaluation',
        EMPLOYEE: 'employee',

        PERSONAL_BASE: 'personal',
        EVALUATED: 'evaluated',
        EVALUATED_TASK: 'evaluated/task'
    };

    public static SETTINGS = {
        BASE: 'settings',

        PERIOD_LIST: 'period',
        PERIOD_EDIT: 'period/edit',
    };

    public static RATING = {
        BASE: 'rate',
        EVALUATION: 'evaluation',
        RATING: 'rating'
    };

    public static TASK = {
        BASE: 'tasks',
        EDIT: 'task/edit',
        CREATE: 'task/create',
        VIEW: 'task'
    };

    public static TO_DO = {
        BASE: 'to_do_list'
    };

    public static generateFullUrl( ...SiteUrl: string[] ) {
        let fullUrl = '';
        if ( SiteUrl && SiteUrl.length > 0 ) {
            SiteUrl.forEach( value => {
                fullUrl += this.PREFIX + value;
            } );
        }
        return fullUrl;
    }
}
