import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LoclizeServiceService } from './shared/loclize-service.service';
import { AppPropService } from './shared/app-prop.service';
import { SharedService } from './shared/shared.service';
import { IdealService } from './shared/ideal.service';
import { routeAnimations } from './shared/animations/index';
import { RouterOutlet, Router } from '@angular/router';
import { NotificationtoasterService } from './shared/notificationtoaster.service';
import { ApiUrlService } from './shared/service-urls/apiurl.service';
 import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { PermissionService } from './shared/permission.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [routeAnimations]
})
export class AppComponent implements OnInit , OnDestroy {

  private stompClient;

  deviceTypes: any[] = [{ key: "lp24", value: "LP24" }, { key: "mp24", value: "MP24" }, { key: "mp96", value: "MP96" }, { key: "dpcr", value: "dPCR Analyzer" }];
  flags = {};


  constructor(
    private translate: TranslateService,
    private _localyzeSvc: LoclizeServiceService,
    private _apiPropSvc: AppPropService,
    private _sharedSvc: SharedService,
    private _idealService: IdealService,
    private _NTS: NotificationtoasterService,
    private _router: Router,
    @Inject('UrlService') private _urlSvc: ApiUrlService,
    private _PS: PermissionService
  ) {

    // translate.addLangs(["en-US", "fr"]);
    translate.setDefaultLang('en');

    // let browserLang = translate.getBrowserLang();
    const browserLang = navigator.language;
    console.log('Lang selected: ', browserLang);
    this._localyzeSvc.supportedLangs.subscribe(langs => {
      translate.use(langs.indexOf(browserLang.toLowerCase()) > -1 ? browserLang : 'en');
    });
  }

  ngOnInit() {
    // To load the Api Properties.
    this.loadAppProperties();
    //this.loadFlags();
  }

  ngOnDestroy() {
    console.log('ngon destroy called in appcomponent');
    this._NTS.disconnectToaster();
  }

  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
  }

  isInitialApiCallNeeded(): boolean{
    let isInitialApiCallNeeded = true;
    let noInitialApiCallForPages = ["login", 'rsr-login'];
    let currentUrl = window.location.href.split("/");
    let currentPage = currentUrl[currentUrl.length - 1];

    if(noInitialApiCallForPages.indexOf(currentPage) > -1){
      isInitialApiCallNeeded = false;
    }

    return isInitialApiCallNeeded;
  }

  public loadFlags(deviceType: any, apiProp: any){
    if(this.isInitialApiCallNeeded()){
      this._apiPropSvc.getFlagsFromAmm(deviceType.value, apiProp).subscribe(successData => {
        if(successData){
          this.flags[deviceType.key] = {};
          successData.forEach(flag => {
            this.flags[deviceType.key][flag.flagCode] = flag.severity;
          });
        }
        this._sharedSvc.setData('flags', this.flags);
      }, error => {
        console.log('Error while loading App Properties');
      });
    }
  }

  public loadAppProperties() {
    this._apiPropSvc.getAppProperties().subscribe(successData => {
      console.log('API PROPS', successData);
      this._sharedSvc.setData('appProperties', successData);
      this._urlSvc.setProperties();
      // start the ideal service
      this._idealService.start();
      this.loadNotifToster();
      // this._PS.loadPermissions();
      this.deviceTypes.forEach(deviceType => {
        this.loadFlags(deviceType, successData);
      });
    }, error => {
      console.log('Error while getting App Properties');
    });
  }

  public loadNotifToster() {
    console.log('loadNotifToster=================');
    this._NTS.loadNotifToster();
  }

}
