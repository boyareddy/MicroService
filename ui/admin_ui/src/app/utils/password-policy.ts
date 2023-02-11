export const passwordPolicy = {
    1: 'special characters required',
    2: 'minimum character required',
    3: 'maximum character required',
    4: 'Numeric character required',
    5: 'start alphabet required',
    6: 'previous password repeated'
};

// tslint:disable-next-line:no-shadowed-variable
export const getPasswordErrorMsgs = (passwordPolicy: any, errors: any) => {
    const errMsgs = [];
    errors.forEach((errorCode, index) => {
        if (errors.length > 1) {
            errMsgs.push(`${index + 1}. ${passwordPolicy[errorCode].charAt(0).toUpperCase() + passwordPolicy[errorCode].slice(1)}`);
        } else {
            errMsgs.push(`${passwordPolicy[errorCode].charAt(0).toUpperCase() + passwordPolicy[errorCode].slice(1)}`);
        }
    });
    return errMsgs;
};
