import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SearchElement } from '../models/search.model';
import { SEARCH_URLS } from '../models/search.url';
import { handleError } from '../../utils/misc.util';
import { SharedService } from '../../services/shared.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  appProperties: any;
  rmmApiEnd: string;

  constructor(private _httpClient: HttpClient, private _sharedSvc: SharedService) { 
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
    // tslint:disable-next-line:max-line-length
    this.rmmApiEnd = `${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.rmmApi.port}/${this.appProperties.rmmApi.module}/json`;
  }

  getOrdersAndRuns(searchInput: string, {offset, content, limit}): Observable<SearchElement>{
    let encodeQueryParam = encodeURIComponent(searchInput);
    return this._httpClient.get<SearchElement>(`${this.rmmApiEnd}/rest/api/v1/search?query=${encodeQueryParam}&offset=${offset}&content=${content}&limit=${limit}`).pipe(catchError(handleError));
    //return this._httpClient.get<SearchElement>(`http://localhost:4000/global-search?offset=${offset}&limit=${limit}`).pipe(catchError(handleError));
    //return this._httpClient.get<SearchResult>(SEARCH_URLS.MOCK.SEARCH).pipe(catchError(handleError));
  }
}
