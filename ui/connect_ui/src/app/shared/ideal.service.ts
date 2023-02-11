import { Injectable } from '@angular/core';
import { UserIdleService, UserIdleConfig } from 'angular-user-idle';
import { Router } from '@angular/router';
import { SharedAuthService } from './shared-auth.service';
import { SharedService } from './shared.service';
import { NotificationtoasterService } from './notificationtoaster.service';

@Injectable({
    providedIn: 'root'
})
export class IdealService {

    constructor(
        private userIdle: UserIdleService,
        private _route: Router,
        private _sharedAuthService: SharedAuthService,
        private _sharedService: SharedService,
        private _NTS: NotificationtoasterService
    ) {}

    start() {
       const _idle = new UserIdleConfig();
       // Get the session time out value from properties file
       _idle.idle = this._sharedService.getData('appProperties').sessionTimeOut;
       _idle.timeout = 1;
       // Setting the configuration values to ideal module
       this.userIdle.setConfigValues(_idle);
        // Start watching for user inactivity.
        this.userIdle.startWatching();
        // Start watching when user idle is starting.
        this.userIdle.onTimerStart().subscribe(count => console.log(count));
        // Start watch when time is up.
        this.userIdle.onTimeout().subscribe(() => {
            const cookieVal = document.cookie.match(new RegExp('(^| )brownstoneauthcookie=([^;]+)'));
            console.log(cookieVal[2], 'cookieVal');
            sessionStorage.removeItem('userPermissions');
            this._sharedAuthService.logout(cookieVal[2]).subscribe(
                success => {
                    this._sharedAuthService.deleteAuthStatus();
                    document.cookie = `brownstoneauthcookie='';path=;expires=Thu, 18 May 2017 12:12:12 GMT;`;
                    this._NTS.disconnectToaster();
                    // Delete all local storage data
                    localStorage.clear();
                    sessionStorage.clear();
                    sessionStorage.setItem('autoLoggedOut', window.location.href);
                    // tslint:disable-next-line:max-line-length
                    window.location.href = `${this._sharedService.getData('appProperties').protocol}://${this._sharedService.getData('appProperties').host}:${this._sharedService.getData('appProperties').appPort}/connect_ui/#/login`;
                },
                error => {
                  console.log('Error on logout');
                }
            );
        });
    }

    stop() {
        this.userIdle.stopTimer();
    }

    stopWatching() {
        this.userIdle.stopWatching();
    }

    startWatching() {
        this.userIdle.startWatching();
    }

    restart() {
        this.userIdle.resetTimer();
    }

}
