import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { Location } from '@angular/common';
import { SearchResult, Order, RunResult, ValidSearchInput, ScrollEvent, SearchElement } from '../../models/search.model';
import { SearchService } from '../../services/search.service';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { SnackbarClasses } from '../../../standard-names/constants';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedService } from 'src/app/shared/shared.service';
import { OrderTableComponent } from '../order-table/order-table.component';
import { RunTableComponent } from '../run-table/run-table.component';

const SEARCH_STORAGE_KEY: string = "query";


@Component({
  selector: 'app-global-search',
  templateUrl: './global-search.component.html',
  styleUrls: ['./global-search.component.scss']
})
export class GlobalSearchComponent implements OnInit {

  searchInput: string;
  afterSearchInput: string;
  orders: Order[];
  runResults: RunResult[];
  isValidSearch: boolean = true;
  isEnter: boolean = false;
  isSprinerOn = false;
  rectPro;
  previousPage;
  invalidSearchError: string;
  apiProp: any;

  // Infinite Scroll Variables
  scrollEvent: ScrollEvent = {} as ScrollEvent;
  contentPerPage: number = 100;
  totalOrders: number;
  totalRuns: number;
  panelClicked: any = {};
  isScrollXhrInProgress: boolean = false;

  @ViewChild('gsInput') gsInput;  

  @ViewChild('orderTable') orderTable: OrderTableComponent;
  @ViewChild('runTable') runTable: RunTableComponent;

  constructor(private _sharedSvc: SharedService,private _matIconReg: MatIconRegistry, private _domSanitizer: DomSanitizer,private _location: Location, private _searchSvc: SearchService, private elem: ElementRef, private _snackBarSvc: SnackBarService, private _acRoute: ActivatedRoute, private _router: Router) { 
    this._matIconReg
      .addSvgIcon('close', 
      this._domSanitizer.bypassSecurityTrustResourceUrl('assets/Images/close-svg.svg'));
  }

  ngOnInit() {
    this.apiProp = this._sharedSvc.getData('appProperties');
    this.contentPerPage = this.apiProp.globalSearch.infiniteScrollQueryLength;
    this.previousPage = sessionStorage.getItem('globalSearchBack');
    this.apiProp = this._sharedSvc.getData('appProperties');
    this._acRoute.queryParams.subscribe(params => {
      const lastSearchInput: string = params[SEARCH_STORAGE_KEY];
	  this.gsInput.nativeElement.focus();
      if(lastSearchInput){
        let decodedSearchInput: string = decodeURIComponent(lastSearchInput);
        this.setSearchInput('searchInput', decodedSearchInput);
        this.setSearchInput('afterSearchInput', decodedSearchInput);
        this.onSearch(lastSearchInput);
      }else{
        this.setSearchInput('searchInput', '');
        this.setSearchInput('afterSearchInput', '');
        this._router.navigate(['/search']);
      }
    })
  }

  loadSpinner() {
    setTimeout(() => {
      if (this.isSprinerOn) {
          const rect = this.elem.nativeElement.querySelectorAll('.gs-container')[0].getBoundingClientRect();
          console.log(rect, 'rect');
          this.rectPro = rect;
      }
    }, 600);
  }

  onClosingSearch(){
    this.previousPage ? window.location.href = this.previousPage : this._location.back();
  }
  
  onSearchNavigate(){
    this._router.navigate(['/search'], { queryParams: { query: encodeURIComponent(this.searchInput) } });
  }

  onSearch($event){
    this.isSprinerOn = true;
    this.loadSpinner();
   let isValidInput: ValidSearchInput = this.validateSearch(this.searchInput.trim(), `${this.apiProp.globalSearch.minQueryLength}`, `${this.apiProp.globalSearch.maxQueryLength}`);
    if(isValidInput.isValid){
      this.isValidSearch = true;

      // Initialize the scroll event.
      this.updateScrollEvent({offset: 0, content: 'both', limit: this.contentPerPage});

      this._searchSvc.getOrdersAndRuns(this.searchInput, this.scrollEvent).subscribe((searchResults: SearchElement) => {
        this.isSprinerOn = false;
        console.log("searchResults", searchResults);
        this.orders = searchResults.searchOrderElements.orders;
        this.runResults = searchResults.searchRunResultElements.runResults;

        // Set the total records
        this.totalOrders = searchResults.searchOrderElements.totalElements;
        this.totalRuns = searchResults.searchRunResultElements.totalElements;
      },error => {
        this.isSprinerOn = false;
        this.runResults = null;
        if(!error){
          this.orders = [];
          this.runResults = [];
        }else{
          if(error instanceof ProgressEvent){
            this._snackBarSvc.showErrorSnackBarWithLocalize('globalSearch.connectionLoss', SnackbarClasses.errorBottom1);
          }else if(typeof error === 'object'){
            this._snackBarSvc.showErrorSnackBarWithLocalize(error.error, SnackbarClasses.errorBottom1);
          }
        }
      });
    }else{
      this.isValidSearch = false
      this.invalidSearchError = isValidInput.error;
      this.isSprinerOn = false;
    }
  }

  validateSearch(input: string, minLength, maxLength): ValidSearchInput{
    let validationResult: ValidSearchInput = {} as ValidSearchInput;
    if(input.length < minLength){
      validationResult.isValid = false;
      validationResult.error = this._snackBarSvc.getMessage('globalSearch.minSearchChar').replace('$$$', minLength);
    }else if(input.length > maxLength){
      validationResult.isValid = false;
      validationResult.error = this._snackBarSvc.getMessage('globalSearch.maxSearchChar').replace('$$$', maxLength);
    }else{
      validationResult.isValid = true;
    }
    return validationResult;
  }

  highlight() {
    let tds = document.getElementsByClassName('ord_row');

    if(tds){
      for(let td in tds){
        let eleTd = <HTMLElement> tds[td];
        let innerHTML = eleTd.innerText;
        if(innerHTML){
          let index = innerHTML.toLowerCase().indexOf(this.searchInput.toLowerCase());
          if (index >= 0) { 
            innerHTML = innerHTML.substring(0,index) + "<span style='background: rgba(0, 78, 163, 0.2);'>" + innerHTML.substring(index, index + this.searchInput.length) + "</span>" + innerHTML.substring(index + this.searchInput.length);
            document.getElementsByClassName('ord_row')[td].innerHTML = innerHTML;
          }
        }
      }
    }
  }

  afterTableLoad(event){
    console.log("afterTableLoad", event);
    this.highlight();
  }

  getSessionStorage(key){
    return JSON.parse(sessionStorage.getItem(key));
  }

  setSessionStorage(key, value){
    sessionStorage.setItem(key, value);
  }

  setSearchInput(key, value){
    this[key] = value;
  }

  // Infinite scroll call
  onScroll(): void{
    let scrollEvent: ScrollEvent = {...this.getScrollEVent(this.panelClicked.order ? this.orderTable : true, this.panelClicked.run ? this.runTable : true, this.orders.length, this.runResults.length, this.totalOrders, this.totalRuns, this.scrollEvent.limit), limit: this.contentPerPage};
    console.log("onScroll",this.panelClicked.order ? (this.orderTable ? true : false) : true, this.panelClicked.run ? (this.runTable ? true : false) : true, this.orders.length, this.runResults.length, this.totalOrders, this.totalRuns, this.scrollEvent.limit, scrollEvent);

    if(scrollEvent.offset !== undefined && scrollEvent.content){this.onSearchXhrCall({offset: scrollEvent.offset, content: scrollEvent.content, limit: scrollEvent.limit});}
  }

  onSearchXhrCall({offset, content, limit}): void{
    this.onToggleSpinner();
    setTimeout(timeOut => {
      this._searchSvc.getOrdersAndRuns(this.searchInput, {offset, content, limit}).subscribe(({searchOrderElements, searchRunResultElements}) => {
        this.onToggleSpinner();
        if(searchOrderElements) this.orders = this.orders.concat(searchOrderElements.orders);
        if(searchRunResultElements) this.runResults  = this.runResults.concat(searchRunResultElements.runResults);
      },error => {
          if(error instanceof ProgressEvent){
            this.onToggleSpinner();
            this._snackBarSvc.showErrorSnackBarWithLocalize('globalSearch.connectionLoss', SnackbarClasses.errorBottom1);
          }else if(typeof error === 'object'){
            this._snackBarSvc.showErrorSnackBarWithLocalize(error.error, SnackbarClasses.errorBottom1);
          }
      });
    }, 1000);
  }

  updateScrollEvent({offset, content, limit}): void{
    this.scrollEvent = {offset, content, limit};
  }

  getScrollEVent(orderTable, runTable, orders, runResults, totalOrders, totalRuns, limit): ScrollEvent{

    let content: string;
    let offset: number;

    if((orderTable && runTable) && orders < totalOrders){
      content = 'order';
      offset = orders;
    }else if((orderTable && runTable) && runResults < totalRuns){
      content = 'run';
      offset = runResults;
    }else if(orderTable && orders < totalOrders){
      content = 'order';
      offset = orders;
    }else if(runTable && runResults < totalRuns){
      content = 'run';
      offset = runResults;
    }

    return {content, offset, limit};
  }

  onToggleSpinner(): void{
    this.isScrollXhrInProgress = !this.isScrollXhrInProgress;
  }

  onPanelToggle(panel): void{
    
    // Check if any scroll bar visible
    if(document.body.scrollHeight > window.innerHeight){
      this.panelClicked[panel] = true;
      setTimeout(timeOut => {
        this.onScroll();
      });
    }
  }
}
