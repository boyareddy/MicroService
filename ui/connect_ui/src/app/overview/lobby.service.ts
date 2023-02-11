import { Injectable } from '@angular/core';
import { CommonApiService } from '../shared/common-api.service';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {

  constructor(private _commonapiservice: CommonApiService) { }

  // getUserRole(userDetails) {
  //   return this._commonapiservice.get('getUserRoleInfo', userDetails);
  // }
}
