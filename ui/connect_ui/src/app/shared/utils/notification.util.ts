
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
    '/orders',
    '/orders/order-details',
    '/orders/create-order/bulk-upload/preview',
    '/workflow',
    '/workflow/pendingruns',
    '/workflow/rundetails',
    '/workflow/mapped-samples',
    '/workflow/containersamples/preview',
    '/overview'
];

export const APPNOTFROUTERURLS = [
    {
        URL: '/orders',
        TOPIC: 'Orders'
    },
    {
        URL: '/workflow',
        TOPIC: 'Workflow'
    },
    {
        URL: '/notification',
        TOPIC: 'All topics'
    },
    {
        URL: '/overview',
        TOPIC: 'All topics'
    },
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
export const HTPTRIGGEREDNF = 'HTP triggered notification';

