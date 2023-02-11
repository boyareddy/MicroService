export enum TosterClasses {
    InfoBottom1 = 'info-toster-bottom1',
    InfoBottom2 = 'info-toster-bottom2',
    WarningBottom1 = 'warn-toster-bottom1',
    WarningBottom2 = 'warn-toster-bottom2',
    ErrorBottom1 = 'error-toster-bottom1',
    ErrorBottom2 = 'error-toster-bottom2'
}

export const NOTFTOPICS = [
    'Orders',
    'Workflow',
    'Connections',
    'Administration'
];

export enum NOTFSEVERITY {
    ERROR = 'Error',
    WARNING = 'Warning',
    INFO = 'Info'
}

export const NOTFTOPICSCONST = {
    ALLTOPICS: 'All topics',
    ORDERS: 'Orders',
    WORKFLOW: 'Workflow',
    CONNECTIONS: 'Connections',
    ADMINISTRATION: 'Administration'
};

export const PARAENTROUTERS = [
    'Orders',
    'Workflow',
    'Notification'
];

export const NTROUTERURLS = [
    '/settings',
    '/add-user',
    '/edit-user',
    '/change-password',
    '/my-profile',
    '/device-list',
    '/device-detail',
    '/edit-device',
    '/reg-device',
    '/audit-log',
    '/report',
    '/about',
    '/search'
];

export const APPNOTFROUTERURLS = [
    {
        URL: '/settings',
        TOPIC: 'Administration'
    },
    {
        URL: '/add-user',
        TOPIC: 'Administration'
    },
    {
        URL: '/edit-user',
        TOPIC: 'Administration'
    },
    {
        URL: '/change-password',
        TOPIC: 'Administration'
    },
    {
        URL: '/my-profile',
        TOPIC: 'Administration'
    },
    {
        URL: '/device-list',
        TOPIC: 'Connections'
    },
    {
        URL: '/device-detail',
        TOPIC: 'Connections'
    },
    {
        URL: '/edit-device',
        TOPIC: 'Connections'
    },
    {
        URL: '/reg-device',
        TOPIC: 'Connections'
    },
    {
        URL: '/audit-log',
        TOPIC: 'Administration'
    },
    {
        URL: '/report',
        TOPIC: 'Administration'
    },
    {
        URL: '/about',
        TOPIC: 'Administration'
    },
    {
        URL: '/search',
        TOPIC: 'Administration'
    }
];

export const multipleNotfMsg = 'There are multiple notifications awaiting confirmation. Please go to Message center to confirm.';
export const HTPTRIGGEREDNF = 'HTP triggered notification';
