import { AuditLog } from "../models/audit.model";

export const auditLogHeaders = () => {
    let auditLogHeaders: AuditLog = {} as AuditLog;

    auditLogHeaders.companydomainname = 'Company domain name';
    auditLogHeaders.id = 'Id';
    auditLogHeaders.messagecode = 'Message code'.replace(/,/g, '');
    auditLogHeaders.newnessflag = 'Newness flag'.replace(/,/g, '');
    auditLogHeaders.objectnewvalue = 'Object new value'.replace(/,/g, '');
    auditLogHeaders.objectoldvalue = 'Object old value'.replace(/,/g, '');
    auditLogHeaders.ownerPropertyName = 'Owner property name'.replace(/,/g, '');
    auditLogHeaders.params = 'Params'.replace(/,/g, '');
    auditLogHeaders.systemid = 'System Id'.replace(/,/g, '');
    auditLogHeaders.systemmodulename = 'System module name'.replace(/,/g, '');
    auditLogHeaders.timestamp = 'Timestamp';
    auditLogHeaders.userid = 'User Id'.replace(/,/g, '');

    return auditLogHeaders;
}