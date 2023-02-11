export default class QueryStrings {
    public static staticQueryStrings: any = {
        ownerId: 'ownerId=1',
        roles: 'roles='
    };

    public static addUserQuery(roles: string[]): string {
        let rolesString = 'roles=';
        roles.forEach((element, ind) => {
            if (ind === 0) {
                rolesString += element;
            } else {
                rolesString += '%2C' + element;
            }
        });
        this.staticQueryStrings.roles = rolesString;
        return `${this.staticQueryStrings.ownerId}&${this.staticQueryStrings.roles}`;
    }
}

export enum SnackbarClasses {
    errorBottom1 = 'failed-snackbar-bottom1',
    errorBottom2 = 'failed-snackbar-bottom2',
    successBottom1 = 'success-snackbar-bottom1',
    successBottom2 = 'success-snackbar-bottom2'
}
