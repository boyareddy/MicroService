import { HttpErrorResponse } from "@angular/common/http";
import { throwError } from "rxjs";

export enum MIME_TYPE{
  'zip' = 'application/zip'
}

export const handleError = (err: HttpErrorResponse) => {
    let errMsg = '';
    if (err.error instanceof Error) {
      errMsg = err.error.message;
    } else {
      errMsg = err.error;
    }
    return throwError(errMsg);
}

export const fileDownload = (fileObject: any, fileName: string) => {
  let anchor = document.createElement('a');
  const url = window.URL.createObjectURL(fileObject);
  anchor.href = url;
  anchor.download = fileName;
  anchor.click();
  window.URL.revokeObjectURL(url);
}