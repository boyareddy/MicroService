/**
 * HeaderInfo represents the Header Information Model
 */
export interface HeaderInfo {
    headerIcon?: string;
    headerName?: string;
    curPage?: string;
    isCloseRequired?: boolean;
    backButton?: boolean;
    isBackRequired?: boolean;
    isLoginPage?: boolean;
    warningBack?: boolean;
    isNotificationPage?: boolean;
    navigateUrl?: string;
    removeSharedData?: string;
    isCardsPage?: boolean;
    message?: string;
    queryParams?: object;
    isBackRequiredWarning?: boolean;
}
