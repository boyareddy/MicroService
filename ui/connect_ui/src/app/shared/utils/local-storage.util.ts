import { HeaderInfo } from "../header.model";

export enum MULTI_NAV {
    "ORDER_DETAIL_PREV" = "order-details_prev"
}

export const setLocal = (key, value) => {
    window.sessionStorage.setItem(key, value);
}

export const getLocal = (key) => {
    return window.sessionStorage.getItem(key);
}

export const setLocalObject = (key, url, param?) => {
    let headerInfo: HeaderInfo = {} as HeaderInfo;
    headerInfo.navigateUrl = url;
    headerInfo.queryParams = param;
    window.sessionStorage.setItem(key, JSON.stringify(headerInfo));
}

export const getLocalObject = (key) => {
    return JSON.parse(window.sessionStorage.getItem(key));
}

export const clearLocal = (key) => {
    window.sessionStorage.removeItem(key);
}

const merge = ( ...objects ) => ( { ...objects } );