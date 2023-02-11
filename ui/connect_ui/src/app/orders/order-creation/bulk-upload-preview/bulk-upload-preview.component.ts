import { Component, OnInit, ViewChild, Input, OnChanges, AfterViewChecked } from '@angular/core';
import { HeaderInfo } from '../../../shared/header.model';
import { SharedService } from 'src/app/shared/shared.service';
import { Router } from '@angular/router';
import { MatSort, MatTableDataSource, MatSnackBar } from '../../../../../node_modules/@angular/material';
import { OrderService } from '../../order.service';
import { environment } from '../../../../environments/environment';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { StringConstants, SnackbarClasses } from '../../../standard-names/constants';
import { TranslateService } from '../../../../../node_modules/@ngx-translate/core';
/**
 * @title Basic use of `<table mat-table>`
 */
@Component({
  selector: 'app-bulk-upload-preview',
  templateUrl: './bulk-upload-preview.component.html',
  styleUrls: ['./bulk-upload-preview.component.scss']
})
export class BulkUploadPreviewComponent implements OnInit, OnChanges, AfterViewChecked {
  // tslint:disable-next-line:max-line-length
  headerInfo: HeaderInfo = {
    headerName: 'Orders',
    isBackRequired: true,
    curPage: 'CSV upload preview'
  };

  @ViewChild(MatSort) sort: MatSort;
  @Input('bulkUploadorders') bulkUploadorders;
  dataSource: MatTableDataSource<any>;
  tempUploadedMapping;
  value;
  // tslint:disable-next-line:max-line-length
  displayedColumns: string[] = ['accessioningid', 'assay', 'sampletype', 'retest', 'comments', 'gesage', 'matage', 'ivfstatus', 'eggdonor', 'eggdonorage', 'fetus', 'collectiondate', 'receiveddate', 'testoptions', 'firstname', 'lastname', 'mednum', 'dob', 'refname', 'clinicname' , 'otherclinician' , 'labid' , 'account' , 'referal'];

  constructor(private _sharedSvc: SharedService,
    private _router: Router,
    private _sharedService: SharedService,
    private _snackBar: MatSnackBar,
    private _snackBarSvc: SnackBarService,
    private _orderService: OrderService,
    private _translate: TranslateService) { }
    objectKeys = Object.keys;

    ngOnChanges() {
      // this.dataSource = new MatTableDataSource(this.bulkUploadorders);
      // this.dataSource.sort = this.sort;
    }

  ngOnInit() {

    // Get the temporarily uploaded data.
    this.tempUploadedMapping = null;
    if (environment.production) {
      this.tempUploadedMapping = this._sharedSvc.getData('tempBulkOrderUpload');
    } else {
      // tempUploadedMapping = './assets/JsonFiles/orders/bulk-order-upload.json';
    }

    // tslint:disable-next-line:max-line-length
     // this.tempUploadedMapping = {"orderParentDTO":[{"order":{"orderId":null,"patientId":null,"patientSampleId":null,"accessioningId":"AXB09999-PATTs","orderStatus":null,"assayType":"NIPTDPCR","sampleType":"Plasma","retestSample":true,"orderComments":"asjkfhasj  hasflashfh asihflasfh ashfaslfhal lashfklasfh lasihfaslfhlash lashflasfhl lashflasfh lashflask fhlashfaslhf aslhfaslkh flashflas lshflashfs","activeFlag":null,"createdBy":null,"createdDateTime":null,"updatedBy":null,"updatedDateTime":null,"assay":{"patientAssayid":null,"maternalAge":40,"gestationalAgeWeeks":16,"gestationalAgeDays":5,"ivfStatus":"Yes","eggDonorAge":41,"eggDonor":"Self","fetusNumber":"2","collectionDate":"2019-01-08T18:30:00.000Z","receivedDate":"2019-01-08T18:30:00.000Z","testOptions":{"Harmony Prenatal Test (T21, T18, T13)":true,"Fetal Sex":true}},"patient":{"patientId":null,"patientLastName":"asdfas fasfasf asfasfasfasfasa","patientFirstName":"asdfas fasfasf asfasfasfasfasa","patientMedicalRecNo":"10000000","patientDOB":"11/11/1970","refClinicianName":"asdfas fasfasf asfasfasfasfasa","otherClinicianName":"asdfas fasfasf asfasfasfasfasa","refClinicianClinicName":"asdfas fasfasf asfasfasfasfasa","accountNumber":"11256","labId":"1000","reasonForReferral":"asdfas fasfasf asfasfasfasfasa","clinicName":null}}}]};
    // tslint:disable-next-line:quotemark
    if (this.tempUploadedMapping) {
      // this.dataSource = new MatTableDataSource(this.bulkUploadorders);
      // this.dataSource.sort = this.sort;
      this.tempUploadedMapping = JSON.parse(this.tempUploadedMapping);
      this.dataSource = (this.tempUploadedMapping).orderParentDTO;

    } else {
      this.onRedirectingToUpload();
    }
  }

  ngAfterViewChecked() {
    if (this.dataSource) {
      this.dataSource.sort = this.sort;
      this.dataSource.sortingDataAccessor = (data, header) => data[header];
    }
  }

  public onReTrying(event) {
    this.onRedirectingToUpload();
  }

  public onConfirming(event) {
    const translations = this._translate.translations[this._translate.currentLang];
    // Get the temporarily uploaded data.
    this._orderService.createBulkOrders(this.tempUploadedMapping).subscribe(successData => {
      this._sharedSvc.setData('bulkordercsvUploadSuccess', successData);
      const message = `${translations.orders.notificationmsg.ordersBulkUpload}`;
      // tslint:disable-next-line:max-line-length
      this._snackBarSvc.showSuccessSnackBar(`${message}`, SnackbarClasses.successBottom2);
      this._router.navigate(['/orders']);
    }, errorData => {
      const message1 = `${translations.orders.notificationmsg.errorBulkUpload}`;
      this.errorSnackBar(`${message1}`);
      console.log('Error on saving the orders.', errorData);
    });
  }

  public onRedirectingToUpload() {
    console.log('isRedirectingToUpload_________________');
    sessionStorage.setItem('isRedirectingToUpload', 'true');
    this._router.navigate(['/orders/create-order']);
  }

  public filterToUpload(previewCSV) {
    const filterCSV = [];
    if (previewCSV) {
      // Get container IDs
      const containerIds = Object.keys(previewCSV);

      containerIds.forEach(containerId => {
        filterCSV.push(previewCSV[containerId]);
      });
    }
    return [].concat.apply([], filterCSV);
  }

  public errorSnackBar(message: string, action?: string) {
    this._snackBar.open(message, action, {
      duration: 6000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['failed-snackbar']
    });
  }

  public checkForTestOptions(inputArr: any) {
    if (inputArr) {
      let testOptionStr = '';
      Object.keys(inputArr).forEach((val, ind) => {
        if (ind > 0 && inputArr[val] === true) {
          if (testOptionStr !== '') {
            testOptionStr = testOptionStr + ', ' + val;
          } else {
            testOptionStr = val;
          }
        } else if ( inputArr[val] === true) {
          testOptionStr = val;
        }
      });
      return testOptionStr;
    }
  }

  public emptyCheck(value) {
    if (value === '' || !value) {
      return true;
    } else {
      return false;
    }
  }
}

