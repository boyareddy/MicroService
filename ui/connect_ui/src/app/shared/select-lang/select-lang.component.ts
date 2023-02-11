import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-select-lang',
  templateUrl: './select-lang.component.html',
  styleUrls: ['./select-lang.component.scss']
})
export class SelectLangComponent implements OnInit {

  selectedLang: string;
  langOptions: any = [
    {langName: 'English', langValue: 'en-US'},
    {langName: 'French', langValue: 'fr'}
  ];

  constructor(private translate: TranslateService) { }

  ngOnInit() {}

  onLangChange(lang: string) {
    this.translate.use(lang);
  }
}
