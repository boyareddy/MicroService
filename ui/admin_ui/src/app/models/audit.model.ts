// tslint:disable-next-line:interface-over-type-literal
export type AuditDateRange = {
    fromDate: string;
    toDate: string;
    label: string | number;
    shLabel: string | number;
};

// tslint:disable-next-line:interface-over-type-literal
export type AuditLog = {
    companydomainname: string;
    id: number | string;
    messagecode: string;
    newnessflag: string;
    objectnewvalue: string;
    objectoldvalue: string;
    ownerPropertyName: string;
    params: string;
    systemid: string;
    systemmodulename: string;
    timestamp: string;
    userid: string;
};

// tslint:disable-next-line:interface-over-type-literal
export type AuditBtn = {
    currentYear: AuditDateRange;
    lastMonth: AuditDateRange;
    lastWeek: AuditDateRange
};
