export type Device = {
    deviceName: string,
    deviceType: string,
    status: string,
    serialNo: string,
    model?: string
}

export type DeviceAttribute = {
    deviceStatus?: string,
    protocol?: string[],
    isRegistered?: boolean,
    model?: string,
    hwVersion?: string,
    protocolVersion?: string,
    location?: string,
    make?: string,
    swVersion?: string
    client?: string;
    password?: string;
    deviceSubStatus?: string;
    JWTCertificate?: string;
    sshCertificate?: string;
    clientCertificate?: string;
    ttv2OutputDirectory?: string;
    url?: string;
}

export type DeviceXhrStatus = {
    loadDevicesInProgress?: boolean,
    addDeviceInProgress?: boolean
}

export type NewDevice = {
    deviceName: string,
    deviceType: string,
    status: boolean,
    serialNo: string,
    attributes: DeviceAttribute
}

export type DeviceForm = {
    name: any;
    deviceType: any
    status: any
    serialNo: any;
    deviceStatus: any;
    hwVersion?: any;
    swVersion?: any;
    isRegistered?: boolean;
    location?: any;
    make?: any;
    model?: any;
    protocol?: any[];
    protocolVersion?: any;
    ipAddress?: any;
    hostName?: any;
    description?: any;
    state?: any;
    site?: any;
    gatewayId?: any;
    clientId?:any;
    password?:any;
    devComplteStatus?:any;
    JWTCertificate?: any;
    sshCertificate?: any;
    clientCertificate?: any;
    ttv2OutputDirectory?: any;
    url?: any;
}

export type DeviceDetailForm = {
    name: string;
    location: string;
}

export type Auth = {
    userDetails: UserDetails;
    password: string;
}

export type UserDetails = {
    loginName: string;
    firstName: string;
    lastName: string;
    email: string;
    contact: any;
    userPreferences: UserPreferences
    password: string;
}

export type UserPreferences = {
    preferenceKey: string;
    value: string;
}