import { Component, OnInit, Input, Output, EventEmitter, AfterViewChecked, ViewChild, OnChanges } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog, MatTableDataSource, MatSort } from '@angular/material';
import { AdminApiService } from '../services/admin-api.service';
import StringConstants from '../utils/string-constants';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ArchiveUser } from '../models/user.model';
import { API_CONSTANTS } from '../utils/api-constants';
import { SharedService } from '../services/shared.service';
import { PermissionService } from '../services/permission.service';

export interface User {
  fullName: string;
  userName: string;
  email: string;
  userRole: string;
}

const ELEMENT_DATA: User[] = [
  {fullName: 'Maria Gonzales', userName: 'MariaG', email: 'maria.g@lab.com', userRole: 'Lab operator'}

];

@Component({
  selector: 'app-user-table',
  templateUrl: './user-table.component.html',
  styleUrls: ['./user-table.component.css']
})
export class UserTableComponent implements OnInit, AfterViewChecked, OnChanges {

  // tslint:disable-next-line:no-output-on-prefix
  @Output() onUserArchiving: EventEmitter<string> = new EventEmitter();
  @Input() userList: any[] = [];
  @Input() archiveList: any[] = [];
  @ViewChild(MatSort) sort: MatSort;
  displayedColumns: string[] = ['firstName', 'loginName', 'email', 'role', 'archive'];
  dataSource: MatTableDataSource<any>;
  appProperties: any;
  loginUsrName;
  havingAccess = false;

  constructor(private _adminApiService: AdminApiService,
    private _router: Router,
    private _dialog: MatDialog,
    private _sharedSvc: SharedService,
    private _permission: PermissionService) {
    this.appProperties = this._sharedSvc.getSharedData('appProperties');
  }

  ngOnInit() {
    this._permission.checkPermissionObs('Update_User').subscribe((res) => {
      console.log(res, 'res^^^^^^^^^^^^');
      this.havingAccess = res;
    });
    this.loginUsrName = sessionStorage.getItem('userName');
  }
  ngAfterViewChecked(): void {
  }

  ngOnChanges() {
    this.dataSource = new MatTableDataSource(this.userList);
    this.dataSource.sort = this.sort;
  }

  public onArchivingUser(user: any): void {
    console.log(user);
    const parsedUser: ArchiveUser = (({ id, loginName, deactivated }) => ({ id, loginName, deactivated }))(user);
    console.log(parsedUser, 'parsedUser1');
    parsedUser.deactivated = true;
    const dialogRef = this._dialog.open(ConfirmDialogComponent, {
      width: '486px',
      height: '172px',
      data: {param: StringConstants.STRING('ARCHIVE_DIALOG_MSG'), action: 'deviceMgmt.archive'}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('After Close > ', result);

      // To check whether user clicks on 'Ok' or 'Cancel' button.
      if (result) {

        // tslint:disable-next-line:no-shadowed-variable
        // this._adminApiService.updateUserStatus(parsedUser).subscribe((result: string) => {
        //   const successMessage = `${parsedUser.loginName} archived successfully`;
        //   this.onUserArchiving.emit(successMessage);
        // }, error => {

        // });

        // Added due to PAS 4.3 upgradation.
        // tslint:disable-next-line:no-shadowed-variable
        this._adminApiService.archiveUser(parsedUser.loginName).subscribe((result: string) => {
          const successMessage = `${parsedUser.loginName} archived successfully`;
          this.onUserArchiving.emit(successMessage);
        }, error => {

        });
      }
    });
  }

  public onActivatingUser(user: any): void {
    const emailForPwdReset: string = user.email;
    // tslint:disable-next-line:max-line-length
    const resetEmailUrl: string = btoa(`${this.appProperties.protocol}://${this.appProperties.host}:${this.appProperties.appPort}${this.appProperties.admin_ui.EMAIL_CODE}`);
    // tslint:disable-next-line:max-line-length
    const parsedUser: ArchiveUser = (({ id, loginName, deactivated, firstName, lastName, email }) => ({ id, loginName, deactivated, firstName, lastName, email }))(user);
    parsedUser.deactivated = false;
    const dialogRef = this._dialog.open(ConfirmDialogComponent, {
      width: '486px',
      height: '172px',
      data: { param: StringConstants.STRING('ACTIVATE_DIALOG_MSG'), action: 'Unarchive' }
    });

    dialogRef.afterClosed().subscribe(result => {

      // To check whether user clicks on 'Ok' or 'Cancel' button.
      if (result) {

        // tslint:disable-next-line:no-shadowed-variable
        this._adminApiService.unArchiveUser(parsedUser.loginName).subscribe((result: string) => {
          this._adminApiService.saveResetEmailDetails ( user.loginName,
                                                        API_CONSTANTS.DOMAIN,
                                                        emailForPwdReset,
                                                        // tslint:disable-next-line:no-shadowed-variable
                                                        resetEmailUrl).subscribe(result => {
            console.log(result);
          }, error => {
            console.log('Error', error);
          });
          const successMessage = `${parsedUser.loginName} activated successfully`;
          this.onUserArchiving.emit(successMessage);
        }, error => {
        });
      }
    });
  }

  public onEditingUser(userName: string): void {
    this._router.navigate(['edit-user', userName]);
  }
}
