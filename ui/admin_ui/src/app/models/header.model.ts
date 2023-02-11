/**
 * HeaderInfo represents the Header Information Model
 */
export interface HeaderInfo {
    headerIcon?: string;
    headerName?: string;
    currPage?: string;
    isBackRequired?: boolean;
    isLoginPage?: boolean;
    backUrl?: string[];
    isAnyFormInvalid?: boolean;
    isHomeNavigateRequired?: boolean;
    disableSubHeadLocalize?: boolean;
	navigateUrl?: string;
	queryParams?: object;
}



