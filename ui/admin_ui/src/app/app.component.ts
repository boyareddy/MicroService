import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LocalizeService } from './shared/localize.service';
import { AppPropService } from './services/app-prop.service';
import { SharedService } from './services/shared.service';
import { IdealService } from './services/ideal.service';
import { routeAnimations } from './shared/animations/index';
import { RouterOutlet, Router } from '@angular/router';
import { NotificationtoasterService } from './services/notificationtoaster.service';
import { multipleNotfMsg } from './utils/notification.util';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [routeAnimations]
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'app';
  private stompClient;

  constructor(
    private translate: TranslateService,
    private _NTS: NotificationtoasterService,
    private _sharedSvc: SharedService,
    private _router: Router,
    private _localyzeSvc: LocalizeService
    ) {
    translate.setDefaultLang('en');
    const browserLang = navigator.language;
    console.log('Lang selected: ', browserLang);
    this._localyzeSvc.supportedLangs.subscribe(langs => {
      translate.use(langs.indexOf(browserLang.toLowerCase()) > -1 ? browserLang : 'en');
    });
  }

  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
  }

  ngOnInit() {
    this.loadNotifToster();
  }

  ngOnDestroy() {
    console.log('ngon destroy called in appcomponent');
    this._NTS.disconnectToaster();
  }

  public loadNotifToster() {
    console.log('loadNotifToster=================');
    this._NTS.loadNotifToster();
  }

}
