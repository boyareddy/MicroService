import { Component, OnInit, Input, Output, EventEmitter, OnChanges, AfterViewChecked, ChangeDetectionStrategy } from '@angular/core';
import { filterOptions } from './filter-options.model';
import { FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { LIST } from './test-cases';
import * as moment from 'moment';

const DEFAULT_FILTER: FilterResult = {
  filterCount: 0,
  filterOptions: undefined
}

type DynamicFormControl = {
  key: AbstractControl
}

export type DateRange = {
  fromDate: string;
  toDate: string;
};

export type FilterResult = {
  filterOptions: any;
  filterCount: number;
  filteredList?: any;
  unFilteredList?: any;
}

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilterComponent implements OnInit, OnChanges, AfterViewChecked {

  filterComponentJson;
  filterForm: FormGroup;
  filterResult: FilterResult;
  storageFilterKey: string = 'filter';
  testOptions: string[] = ["India", "USA"];
  startDateRange: DateRange = {} as DateRange;
  endDateRange: DateRange = {} as DateRange;

  @Input() tab: string = "completed";
  @Input() entityList: any[] = [];
  @Input() toggleComponent: boolean = false;

  @Output() onFilter: EventEmitter<FilterResult> = new EventEmitter();
  @Output() onFilterOptionCount: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
    this.startDateRange.toDate = this.endDateRange.toDate = moment().format();
    let storedFilters: FilterResult = this.getSessionStore(this.storageFilterKey);
    //console.log("filterOptions", filterOptions);
    if(this.tab){
      this.filterComponentJson = filterOptions.filter(filterOption => filterOption.tab === this.tab)[0];

      this.filterForm = new FormGroup(this.getFormControls(this.filterComponentJson.filterOptions));
    }

    if(storedFilters && storedFilters.filterOptions){
      this.setFormControls(storedFilters.filterOptions);
      this.emitChanges(storedFilters);
    }
  }

  ngAfterViewChecked(){
    let sessionStore = this.getSessionStore(this.storageFilterKey);
    if(sessionStore){
      let filterOptionCount = sessionStore.filterCount;
      this.onFilterOptionCount.emit(filterOptionCount);
    }

    let storedFilters: FilterResult = this.getSessionStore(this.storageFilterKey);
    if(storedFilters && storedFilters.filterOptions && Object.keys(storedFilters.filterOptions).length > 0){
      this.setFormControls(storedFilters.filterOptions);
      this.emitChanges(storedFilters);
    }else{
      this.clearAllFilter();
    }
  }

  ngOnChanges(){
    this.onFieldChange();
  }

  getFormControls(filterOptions): any{
    let formControls = {};
    for(let group of filterOptions){
      for(let control of group.options){
        formControls[control.fieldKey] = new FormControl(control.fieldValue);
      }
    }
    return formControls;
  }

  getFilteredList(list: any[], {filterOptions}): any[]{
    return list.filter(item => {
      for(let option in filterOptions){
        if(item[option] !== filterOptions[option]){
          return null;
        }
      }
      return item;
    });
  }

  setFormControls(filterOptions): void{
    if(filterOptions){
      for(let filterOption in filterOptions){
        this.filterForm.controls[filterOption].setValue(filterOptions[filterOption]);
      }
    }
  }

  getDropDownOptions(key: string, list: any[]): string[]{
    //return list.map(element => element[key]).filter(this.onlyUnique);
    if(!list){ return [] }

    return list.reduce((a,b) => {
      let keys = Object.keys(b);
      keys.forEach(v => {
          if (Array.isArray(b[v])) a = a.concat(this.getKeyValues(b[v], key));
          if(b[v]){
            if (v === key) a = a.concat(key === 'operatorName' ? b[v].toLowerCase() : b[v]);
          }
      });
      return a;
    }, []).filter(this.onlyUnique);
  }

  getKeyValues(arr, key) {
    return arr.reduce((a,b) => {
        let keys = Object.keys(b);
        keys.forEach(v => {
            if (Array.isArray(b[v])) a = a.concat(this.getKeyValues(b[v], key));
            if(b[v]){
              if (v === key) a = a.concat(key === 'operatorName' ? b[v].toLowerCase() : b[v]);
            }
        });
        return a;
    }, [])
  }

  getSessionStore(key){
    return JSON.parse(localStorage.getItem(this.tab.toLowerCase() + key));
  }

  onlyUnique(value, index, self) { 
    return self.indexOf(value) === index;
  }

  setSessionStore(key: string, value: any){
    localStorage.setItem(this.tab.toLowerCase() + key, JSON.stringify(value));
  }

  clearSessionStore(key: string){
    localStorage.removeItem(this.tab.toLowerCase() + key);
  }

  isFilterApplied(): boolean{
    if(this.getSessionStore(this.storageFilterKey)){
      return true;
    }else{
      return false;
    }
  }

  compareLists(firstList: any[], secondList: any[]): boolean{
    if(JSON.stringify(firstList) === JSON.stringify(secondList)){
      return true;
    }else{
      return false;
    }
  }

  getFilterResult(selectedFilters): FilterResult{
    let filterResult: FilterResult = {} as FilterResult;
    let validKeyCount: number = 0;
    if(selectedFilters){
      for(let selectedFilter in selectedFilters){
        if(!selectedFilters[selectedFilter]){
          delete selectedFilters[selectedFilter];
        }else{
          validKeyCount++;
        }
      }
    }
    filterResult.filterOptions = selectedFilters;
    //filterResult.filterCount = validKeyCount;
    filterResult.filterCount = this.getOptionCount(selectedFilters);
    return filterResult;
  }

  getOptionCount(options: any): number{
    let keys: string[];
    let nonDateOptions: string[];
    let dateOptions: string[];
    let optionCount: number;
    if(options){
      keys = Object.keys(options);
      nonDateOptions = keys.filter(key => key.toLowerCase().indexOf('date') === -1);
      dateOptions = keys.filter(key => key.toLowerCase().indexOf('date') > -1);
      optionCount = nonDateOptions.length + (dateOptions.length === 0 ? 0 : 1);
    }
    return optionCount;
  }

  setFilterResult(filterValue: FilterResult): void{
    this.filterResult = filterValue;
  }

  clearAllFilter(){
    this.clearSessionStore(this.storageFilterKey);
    this.filterForm.reset();
    this.setFilterResult(DEFAULT_FILTER);
    this.emitChanges(this.filterResult);
  }

  onFieldChange(){
    if(this.filterForm && this.filterForm.value){
      let selectedFilters: FilterResult = this.getFilterResult(this.filterForm.value);
      this.setSessionStore(this.storageFilterKey, selectedFilters);
      this.filterResult = selectedFilters;
      this.emitChanges(selectedFilters);
    }
  }

  emitChanges(filterResult: FilterResult): void{
    this.onFilter.emit(filterResult);
  }

  onDateChange(dateType, rangeType, event): void {
    this[dateType][rangeType] = moment(event.value).format();
    this.setSessionStore(this.storageFilterKey, this.getFilterResult(this.filterForm.value));
  }
}
