import { Component, OnInit, Input, Output, EventEmitter, AfterViewInit, AfterViewChecked } from '@angular/core';
import { RunResult, RUN_TABLE_FIELDS } from '../../models/search.model';
import { MULTI_NAV, setLocal, setLocalObject, clearLocal } from '../../../utils/local-storage.util';
import { SharedService } from '../../../services/shared.service';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-run-table',
  templateUrl: './run-table.component.html',
  styleUrls: ['./run-table.component.scss']
})
export class RunTableComponent implements OnInit, AfterViewInit, AfterViewChecked {

  dataSource: RunResult[];
  displayColumns: string[] = RUN_TABLE_FIELDS;
  selectedId = '';
  apiProps: any;

  @Input() runResults: RunResult[];
  @Input() searchQuery: number;
  @Input() isScrollXhrOnProgress: boolean = false;

  @Output() afterTableLoad: EventEmitter<boolean> = new EventEmitter(false);

  constructor(private _sharedSV: SharedService, private _router: Router) { 
    this.apiProps = this._sharedSV.getSharedData('appProperties');
  }

  ngOnInit() {
  }

  onNavigation(entity){
    if(entity.runStatus && entity.runStatus.toLocaleLowerCase() === 'pending'){
      window.open(`${this.apiProps.protocol}://${this.apiProps.host}:${this.apiProps.appPort}/connect_ui/#/workflow/mapped-samples?plateNo=${entity.container.containerType}&containerId=${entity.container.containerId}`, `_self`);
    }else{
      window.open(`${this.apiProps.protocol}://${this.apiProps.host}:${this.apiProps.appPort}/connect_ui/#/workflow/rundetails/${entity.runResultId}`, `_self`);
    }
  }

  ngOnChanges(){
    this.dataSource = this.runResults;
  }

  ngAfterViewInit(){
    this.afterTableLoad.emit(true);
  }

  ngAfterViewChecked(){
    this.afterTableLoad.emit(true);
  }
}
