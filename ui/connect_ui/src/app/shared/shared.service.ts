import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  data: any = {};
  constructor() { }

  getData(key) {
    return this.data[key];
  }

  setData(key, value) {
    this.data[key] = value;
  }

  deleteData(key) {
    delete this.data[key];
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
