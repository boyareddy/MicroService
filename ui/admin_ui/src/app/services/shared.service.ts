import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  // Global shared object
  sharedData: any = {};
  constructor() { }

  /**
   * setSharedData() sets a value in global shared data.
  */
  public setSharedData(key: string, value: any): void {
    this.sharedData[key] = value;
  }

  /**
   * getSharedData() gets a value from the global shared data.
  */
  public getSharedData(key: string): any {
    return this.sharedData[key];
  }

  /**
   * deleteSharedData() delets a value from the global shared data.
  */
  public deleteSharedData(key: string) {
    delete this.sharedData[key];
  }

  getCookieToken() {
    const appCookie = document.cookie;
    let jwtCookie;
    // To check the availability of the cookie
    if (appCookie && appCookie !== '') {
      // Get the token.
      const jwt = appCookie.split(';');
        for (const ele of jwt) {
          if (ele.indexOf('brownstoneauthcookie') > -1) {
            jwtCookie = ele.split('=')[1];
            return jwtCookie;
          }
        }
    }
  }
}
