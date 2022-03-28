export enum TaskStatuses {
    PARAMETERIZATION = 'PARAMETERIZATION',
    UNDER_EVALUATION = 'UNDER_EVALUATION',
    EVALUATED = 'EVALUATED',
    CLOSED = 'CLOSED',
    DELETED = 'DELETED'
}

export enum TaskTypes {
    NORMAL = 'NORMAL',
    AUTOMATIC = 'AUTOMATIC',
    TEMPLATE = 'TEMPLATE'
}

export enum ViewMode {
    VIEW,
    EDIT,
    CREATE
}

export enum AutoCompletType {
    USER = 'user',
    COMPANY = 'company',
    DIFFICULTY = 'difficulty',
    COMPANYVIRTUE = 'companyVirtue',
    LEADERVIRTUE = 'leaderVirtue',
    FACTOR = 'factor'
}

export enum ReferenceType {
    TODO = 'TODO',
    TASK = 'TASK',
    EVALUATION = 'EVALUATION',
    RATING = 'RATING',
    LEADERVIRTUE = 'LEADERVIRTUE',
    PERIOD = 'PERIOD'
}

export enum ToDoType {
    EVALUATION = 'EVALUATION',
    RATING = 'RATING'
}

export enum ToDoStatus {
    OPEN = 'OPEN',
    CLOSE = 'CLOSE',
    EXPIRED = 'EXPIRED'
}

export enum NotificationStatus {
    INFORMATION = 'INFORMATION',
    OPEN = 'OPEN',
    CLOSE = 'CLOSE',
    EXPIRED = 'EXPIRED'
}
