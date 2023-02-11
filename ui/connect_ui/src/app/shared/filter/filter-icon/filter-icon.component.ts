import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-filter-icon',
  templateUrl: './filter-icon.component.html',
  styleUrls: ['./filter-icon.component.scss']
})
export class FilterIconComponent implements OnInit {

  toggle: boolean = false;

  @Input() componentToToggle: any;
  @Input() optionCount: number;
  @Input() display: boolean;

  @Output() onToggleFilterIcon: EventEmitter<boolean> = new EventEmitter();

  constructor(private _matIconReg: MatIconRegistry, private _domSanitizer: DomSanitizer,) { 
    this._matIconReg
      .addSvgIcon('filter', 
      this._domSanitizer.bypassSecurityTrustResourceUrl('assets/Images/filter.svg'));
  }

  ngOnInit() {}

  toggleComponent(): void{
    this.toggle = !this.toggle;
    if(this.componentToToggle){
      this.componentToToggle.toggleComponent = !this.componentToToggle.toggleComponent;
    }else{
      this.onToggleFilterIcon.emit(this.toggle);
    }
  }
}
