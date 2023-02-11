/**
 * StringConstants is the class which holds the information of all constants used in the application
 * It reduces the complexity of the application
 */
export default class StringConstants {

    // stringLibrary is the object which hold the all constants as an object
    public static stringLibrary: any = {
        ARCHIVE_DIALOG_MSG: 'Are you sure you want to archive the selected user',
        ACTIVATE_DIALOG_MSG: 'Are you sure you want to unarchive the selected user',
        USER_EXIST: 'Username already exists.',
        User_ADD_SUCCESS: 'User created successfully.'
    };

    /**
     * STRING() takes msgKey as input and returns the matched value from the stringLibrary
     * @param msgKey
     */
    public static STRING(msgKey: string): string {
        return this.stringLibrary[msgKey];
    }
}
