import { Injectable } from '@angular/core';
import { AuthService } from '../auth/AuthService/auth.service';
import { ex_URL_Permission } from '../shared/utils/exceptionURL.const';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class PermissionService {
    userPermissions;
    constructor(
        private _authService: AuthService
    ) {}

    checkPermissionObs(moduleName: string): Observable<boolean> {
        console.log(moduleName, 'moduleName--------');
        console.log(this.getPermissions(), 'this.userPermissions--------');
        if (this.getPermissions()) {
            const userPermissionsArr = this.getPermissions().split(',');
            if (userPermissionsArr.indexOf(moduleName) !== -1) {
                return of(true);
            } else {
                return of(false);
            }
        } else {
            console.log(this.checkURL(), 'this.checkURL()$$$$$$$$$$');
            if (!this.checkURL()) {
               return this._authService.getPermissions().pipe(
                map((res1) => {
                    if (res1) {
                        console.log(res1.toString(), 'res1.toString()');
                        this.setPermissions(res1.toString());
                        const userPermissionsArr = this.getPermissions().split(',');
                        if (userPermissionsArr.indexOf(moduleName) !== -1) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }),
                catchError((error) => {
                    console.log(error, 'checkPermission error');
                    return of(false);
                }));
            } else {
                return of(false);
            }
        }
    }

    setPermissions(permissions) {
        console.log(this.userPermissions, 'setPermissions');
        this.userPermissions = permissions;
    }

    getPermissions() {
        return this.userPermissions;
    }

    loadPermissions() {
        if (!this.checkURL()) {
            this._authService.getPermissions().subscribe((res1) => {
                if (res1) {
                    console.log(res1.toString(), 'res1.toString()');
                    this.setPermissions(res1.toString());
                }
            }, error1 => {
                console.log(error1, 'checkPermission error');
            });
        }
    }

    checkURL() {
        const cur_url_arr = window.location.href.split('/');
        const cur_url = cur_url_arr[cur_url_arr.length - 1];
        if (ex_URL_Permission.length > 0 && cur_url && ex_URL_Permission.indexOf(cur_url) !== -1) {
          return true;
        } else {
          return false;
        }
    }

}
