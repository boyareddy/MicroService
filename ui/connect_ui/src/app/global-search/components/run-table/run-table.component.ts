import { Component, OnInit, Input, Output, EventEmitter, AfterViewInit, AfterViewChecked } from '@angular/core';
import { RunResult, RUN_TABLE_FIELDS } from '../../models/search.model';
import { SharedService } from '../../../shared/shared.service';
import { MULTI_NAV, setLocal, setLocalObject, clearLocal } from '../../../shared/utils/local-storage.util';
import { Router } from '@angular/router';

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
    this.apiProps = this._sharedSV.getData('appProperties');
  }

  ngOnInit() {
  }

  onNavigation(entity){
    // if(entity.runStatus && entity.runStatus.toLocaleLowerCase() === 'pending'){
    //   window.open(`${this.apiProps.protocol}://${this.apiProps.host}:${this.apiProps.appPort}/connect_ui/#/workflow/mapped-samples?plateNo=${entity.container.containerType}&containerId=${entity.container.containerId}`, `myNewWindow${entity.runResultId}`);
    // }else{
    //   window.open(`${this.apiProps.protocol}://${this.apiProps.host}:${this.apiProps.appPort}/connect_ui/#/workflow/rundetails/${entity.runResultId}`, `myNewWindow${entity.runResultId}`);
    // }
   
    this.selectedId = entity.runResultId;
     setLocalObject(MULTI_NAV.ORDER_DETAIL_PREV, 'search', {query: this.searchQuery});
     if(entity.runStatus && entity.runStatus.toLocaleLowerCase() === 'pending'){
     this._router.navigate(['workflow', 'mapped-samples', {plateNo : entity.container.containerType}, {containerId : entity.container.containerId}]);
     } else{
      this._router.navigate(['workflow', 'rundetails', entity.runResultId]);
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
