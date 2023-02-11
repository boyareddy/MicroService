import { Component, OnInit } from '@angular/core';
import { AdminApiService } from '../../services/admin-api.service';
import { NotificationtoasterService } from 'src/app/services/notificationtoaster.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {
  private stompClient;
  constructor(
    private _adminApiSvc: AdminApiService,
    private _NTS: NotificationtoasterService
    ) { }

  ngOnInit() { }

  /**
   * onLogout calls for logging out from the application.
   */
  public onLogout() {
    // Get the cookie.
    const appCookie = document.cookie;

    // To check the availability of the cookie
    if (appCookie && appCookie !== '') {
      // Get the token.
      // let jwt = document.cookie.split(';')[0].split('=');
      const jwt = appCookie.split(';')[0].split('=')[1];
      // let formToken = {token: jwt};

      sessionStorage.removeItem('userPermissions');
      // Call the API service for Logout by pasing the token.
      this._adminApiSvc.logout(jwt).subscribe(
        success => {
          this._NTS.disconnectToaster();
          // Delete all local storage data
          localStorage.clear();
          sessionStorage.clear();

          // Delete "brownstoneauthcookie" cookie.
          this.deleteCookie('brownstoneauthcookie');

          // Delete "JSESSIONID" cookie.
          // this.deleteCookie('JSESSIONID');

          // Navigate to login page.
          window.location.href = `/connect_ui/#/login`;
        },
        error => {
          console.log('Error on logout');
        }
      );
    }
  }

  /**
   * deleteCookie() calls to delete the cookie from the browser.
   */
  public deleteCookie(cookieName) {
    document.cookie = cookieName + '=;Path=/;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    // document.cookie = `brownstoneauthcookie='';path=`;
  }
}

