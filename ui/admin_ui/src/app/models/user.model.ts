export interface User {
    firstName: string;
    lastName: string;
    loginName: string;
    email: string;
    userRole: string;
    userPreferences: any[];
    retired: boolean;
}

export interface EditUser {
    id: number;
    firstName: string;
    lastName: string;
    loginName: string;
    email: string;
    userRole: any[];
    retired: boolean;
    userPreferences: any[];
}

export interface ArchiveUser {
    id: number;
    loginName: string;
    deactivated: boolean;
    firstName?: string;
    lastName?: string;
    email?: string;
}

export interface UserList {
    activeUser: User[];
    archivedUser: User[];
}

