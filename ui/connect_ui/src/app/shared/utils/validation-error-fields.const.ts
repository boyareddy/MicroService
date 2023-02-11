export const errorFields = {
    'dev_name_alpha_num': {
        errorCode: 'Only alphanumaric is allowed.'
    },
    'required': {
        errorCode: 'Please enter the'
    },
    'requiredSelect': {
        errorCode: 'Please select the'
    },
    'allow_dot': {
        errorCode: 'Only dot is allowed within'
    },
    'location': {
        errorCode: { errorCode: 'Allowed special characters : Hypen, apostrophe and one space within location.' }
    },
    'range': {
        errorCode: 'Please select a date within 3 weeks from the Start date'
    }
};

export const errorRegxFields = {
    'allow_special': {
        errorCode: { errorCode: 'Allowed special characters : Hypen, apostrophe and one space within model.' },
        regx: /^[a-zA-Z0-9- ']*$/
    }
};

export const errFieldNames = {
    'devName': 'Device name',
    'devSlNo': 'Device serial number',
    'devStatus': 'Device status',
    'devType': 'Device type',
    'devHostName': 'Host name',
    'devIpAddress': 'IP address',
    'devSwVersion': 'Software version',
    'devHwVersion': 'Hardware version',
    'devProtoVersion': 'Protocol version',
    'startDate': 'Start date',
    'endDate': 'End date'
};
