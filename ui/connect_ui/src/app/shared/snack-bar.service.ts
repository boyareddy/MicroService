import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { CustomSnackbarComponent } from './custom-snackbar/custom-snackbar.component';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  constructor(private _snackBar: MatSnackBar,  private _translateSvc: TranslateService) { }

  /**
   * Display the snackbar
   * @param message Message Info
   * @param className Class [Either success-snackbar-bottom1/ success-snackbar-bottom2]
   */
  public showSuccessSnackBar(message: string, className: string): void {
    this._snackBar.openFromComponent(CustomSnackbarComponent, {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className]
    });
  }

  public showSuccessSnackBarWithLocalize(messageCode: string, className: string): void {
    let message = this.getMessage(messageCode);
    this._snackBar.openFromComponent(CustomSnackbarComponent,  {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className]
    });
  }

  public showSuccessSnackBarWithLocalizeAndReplace(messageCode: string,replaceFor: string, replaceWith: string, className: string): void {
    let message = this.getMessage(messageCode).replace(replaceFor, replaceWith);
    this._snackBar.openFromComponent(CustomSnackbarComponent,  {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className]
    });
  }

  /**
   * Display the snackbar
   * @param message Message Info
   * @param className Class [Either failed-snackbar-bottom1/ failed-snackbar-bottom2]
   */
  public showErrorSnackBar(message: string, className: string): void {
    this._snackBar.openFromComponent(CustomSnackbarComponent , {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className]
    });
  }

  public showErrorSnackBarWithLocalize(messageCode: string, className: string){
    let message = this.getMessage(messageCode)
    this._snackBar.openFromComponent(CustomSnackbarComponent, {
      data: message,
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [className]
    });
  }

  public getMessage(messageCode: string){
    let currentLang = this._translateSvc.currentLang;
    let keys = messageCode.split('.');
    let message = this._translateSvc.translations[currentLang][keys[0]][keys[1]];
    return message;
  }

  public getSingleMessage(messageCode: string){
    let currentLang = this._translateSvc.currentLang;
    let message;
    if(this._translateSvc.translations[currentLang] && this._translateSvc.translations[currentLang][messageCode]){
      message = this._translateSvc.translations[currentLang][messageCode];
    }else{
      message = messageCode;
    }
    return message;
  }
}
