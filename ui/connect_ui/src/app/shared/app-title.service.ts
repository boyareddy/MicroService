import { Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class AppTitleService {

  constructor(private titleService: Title,
              private translate: TranslateService) { }
  pageTitle = 'Roche Conenct';

  getTitle() {
      this.translate.get('Roche Connect', 'Roche Connect').subscribe((res) => {
        this.pageTitle = res;
          console.log(this.pageTitle);
          this.setTitle(this.pageTitle);
      }, err => {
        console.log('error trans', err);
        this.setTitle(this.pageTitle);
      });
  }

  public setTitle(newTitle) {
    this.titleService.setTitle(newTitle);
  }

}
