import { Component, OnInit, Input, ViewChild, OnChanges, ElementRef, OnDestroy } from '@angular/core';
import { HeaderInfo } from '../../shared/header.model';
import { TranslateService } from '../../../../node_modules/@ngx-translate/core';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource, MatSnackBar, MatSort } from '../../../../node_modules/@angular/material';
import { NotificationService } from '../notification.service';
import { XhrProgressLoaderService } from '../../shared/xhr-progress-loader.service';
import { SharedService } from '../../shared/shared.service';
import { SnackBarService } from 'src/app/shared/snack-bar.service';
import { SnackbarClasses } from '../../standard-names/constants';
import { Router } from '../../../../node_modules/@angular/router';
import { HeaderComponent } from '../../shared/header/header.component';
import { AppNotificationService } from 'src/app/shared/appnotification.service';
import { NOTFTOPICSCONST, NOTFSEVERITY, HTPTRIGGEREDNF } from '../../shared/utils/notification.util';
import { NotificationtoasterService } from 'src/app/shared/notificationtoaster.service';


export interface PeriodicElement {
  severity: string;
  topic: string;
  createdDateTime: string;
  title: string;
}



@Component({
  selector: 'app-notification-message',
  templateUrl: './notification-message.component.html',
  styleUrls: ['./notification-message.component.scss']
})

export class NotificationMessageComponent implements OnInit, OnDestroy {
  dataSource: MatTableDataSource<any>;
  dataSource1: any = [];
  public notificationList: any;
  subcribeNotification;
  selectedNotfication;
  notificationRes;
  selected: any;
  dropResponse: any = [];
  notificationCount: any;
  selectedItem = [];
  rowData;
  selectedDropdown = {value: NOTFTOPICSCONST.ALLTOPICS};
  dataSourceViewed: any;
  @Input() listCount: any;
  selectedIndex;
  selectedTab = 0;
  dropdownCount;
  dropdownResp;
  dropdownValue;
  numSelected;
  @ViewChild('header') header: HeaderComponent;
  @ViewChild(MatSort) sort: MatSort;
  isCheckBoxClicked = false;
  isrowSelectedRow = null;
  public completedAll;
  NF_ROUTER;
  isDesCarEnabled = false;
  isHTPDeviceMSGT = false;
  HTPTRIGGEREDNF;

  headerInfo: HeaderInfo = {
    isBackRequired: true,
    isNotificationPage: true,
    curPage: 'Messages',
    isLoginPage: false
  };

  displayedColumns: string[] = ['select', 'severity', 'topic', 'createdDateTime', 'title'];
  selection = new SelectionModel<PeriodicElement>(true, []);

  constructor(private translate: TranslateService,
    private _notificationservice: NotificationService,
    private _xhrLoaderSvc: XhrProgressLoaderService,
    private _sharedSvc: SharedService,
    private _snackBarSvc: SnackBarService,
    private _snackBar: MatSnackBar,
    private _router: Router,
    private _APPNS: AppNotificationService,
    private _NTS: NotificationtoasterService
  ) {
    this.selected = NOTFTOPICSCONST.ALLTOPICS;
    this._NTS.NTMSGLIST = [];
    this.HTPTRIGGEREDNF = HTPTRIGGEREDNF;
  }

  ngOnInit() {
    this.onLoadingNotificationList();
    this.getDropdownValue();
  }

  ngOnDestroy() {
    if (this.subcribeNotification !== undefined) {
      this.subcribeNotification.unsubscribe();
    }
  }

  selectDDByTopicValue() {
    this.NF_ROUTER = sessionStorage.getItem('NF_ROUTER');
    sessionStorage.removeItem('NF_ROUTER');
    console.log(this.NF_ROUTER, 'this.NF_ROUTER');
    if (this.NF_ROUTER) {
      const topicVal = this._APPNS.getTopicByRouter(this.NF_ROUTER);
      if (topicVal) {
        this.selected = topicVal;
        console.log(this.selected, 'this.selected');
        this.onChangeTopic({value: this.selected});
      }
    }
  }

  setSelectedNT() {
    let selectedNotf: any = sessionStorage.getItem('selectedNotf');
    sessionStorage.removeItem('selectedNotf');
    console.log(selectedNotf);
    if (selectedNotf) {
      selectedNotf = JSON.parse(selectedNotf);
      console.log(selectedNotf.id, 'selectedNotf@@@@@@@@@@');
      console.log(this.notificationList, 'this.notificationList!!!!!!!!!!!!!!!!!!!!');
      const SLNT = this.notificationList.findIndex(elem => elem.id === selectedNotf.id);
      console.log(SLNT, 'SLNT6666666666666666666666666');
      if (SLNT) {
        this.getRowData(this.notificationList[SLNT]);
      }
    }
  }


  isAllSelected() {
    this.numSelected = this.selection.selected.length;
    let numRows = null;
    if (this.dataSource) {
      numRows = this.dataSource.data.length;
    }
    if (this.dataSource && this.dataSource.data.length > 0) {
      numRows = this.dataSource.data.filter((elm) => (elm.title !== HTPTRIGGEREDNF)).length;
    }
    if (this.selection.selected.length === 1) {
      this.isCheckBoxClicked = false;
    } else {
      this.isrowSelectedRow = null;
      this.isCheckBoxClicked = true;
    }
    console.log(this.numSelected, 'this.numSelected^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^');
    console.log(numRows, 'numRows^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^');
    return this.numSelected === numRows;
  }

  getCode(row) {
    this.isDesCarEnabled = true;
    if (this.selectedTab !== 1) {
      console.log(this.selection, 'this.selection');
      this.selectedNotfication = row;
      this.selection.toggle(row);
      console.log(this.selection.selected.length, 'this.selection.selected.length');
      console.log(this.isrowSelectedRow, 'this.isrowSelectedRow');
      if (this.selection.selected.length === 1) {
        this.isCheckBoxClicked = false;
        this.rowData = this.selection.selected[0];
      } else {
        this.isCheckBoxClicked = true;
        this.rowData = null;
        this.isrowSelectedRow = null;
      }
    } else {
      this.selectedNotfication = row;
      console.log(this.selectedNotfication , 'selectedNotification');
    }
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
    } else {
      this.isDesCarEnabled = true;
      this.dataSource.data.forEach(row => {
        if (row.title !== HTPTRIGGEREDNF) {
          console.log(row.topic);
          this.selection.select(row);
        }
      });
    }
    this.selectedNotfication = this.selection;
  }


  onTabChanged(event) {
    this.isDesCarEnabled = false;
    this.isrowSelectedRow = null;
    if (this.dataSourceViewed) {
      console.log(this.selectedTab, 'this.selectedTab');
      this.selectedTab = event.index;
      if (this.selectedTab === 1) {
        console.log(event.index, 'event-----------------');
        this.displayedColumns = ['severity', 'topic', 'createdDateTime', 'title'];
        console.log(this.dataSourceViewed, 'viewd datasource');
        this.dropResponse = this.dataSourceViewed;
        this.isHTPDeviceMSGTriggered(this.dataSourceViewed);
        this.dataSource = new MatTableDataSource<PeriodicElement>(this.dataSourceViewed);
        this.selected = NOTFTOPICSCONST.ALLTOPICS;
        setTimeout(() => {
          if (this.dataSourceViewed.length > 0) {
            this.setScrollToTop();
          }
        }, 400);
      } else {
        this.selection.clear();
        console.log(event.index, 'event+++++++++++++++');
        this.dropResponse = this.notificationList;
        this.displayedColumns = ['select', 'severity', 'topic', 'createdDateTime', 'title'];
        this.isHTPDeviceMSGTriggered(this.notificationList);
        this.dataSource = new MatTableDataSource<PeriodicElement>(this.notificationList);
        console.log(this.notificationList, 'viewd notificationList');
        this.selected = NOTFTOPICSCONST.ALLTOPICS;
        setTimeout(() => {
          if (this.notificationList.length > 0) {
            this.setScrollToTop();
          }
        }, 400);
      }
      this.rowData = null;
      this.dataSource.sort = this.sort;
      this.selectedNotfication = null;
      if (this.dropResponse.length > 0) {
        this.getRowData(this.dropResponse[0]);
      }
    }
  }

  setScrollToTop() {
    const myDiv = document.getElementsByTagName('table');
    console.log(myDiv, 'tbl component');
    if (myDiv !== null) {
      myDiv[0].scrollIntoView();
    }
  }

  onLoadingNotificationList() {
    this.subcribeNotification = this._notificationservice.getNotifications().subscribe(resp => {
      this.isDesCarEnabled = false;
      this._xhrLoaderSvc.closeProgress();
      if (this.dataSourceViewed !== null && this.dataSourceViewed !== undefined && this.dataSourceViewed !== '') {
        if (JSON.stringify(this.dataSourceViewed).toLowerCase() !== JSON.stringify(resp).toLowerCase()) {
          this.setNotificationResponse(resp);
        }
      } else {
        console.log(this.dataSourceViewed, 'this.dataSourceViewed else');
        this.setNotificationResponse(resp);
      }
    }, error => {
      this._xhrLoaderSvc.closeProgress();
      console.log('Error on unassigned getting Notification List', error);
    });
  }

  setNotificationResponse(resp) {
    if (resp && resp.length > 0) {
      this.selection.clear();
      this.dataSourceViewed = resp;
      if (this.selectedTab !== 1) {
        this.notificationList = this.dataSourceViewed.filter(
        res => (res.viewed === 'N' && res.severity !== NOTFSEVERITY.INFO));
        if (this.notificationList.length > 0) {
          this.getRowData(this.notificationList[0]);
        }
      }
      if (this.notificationList && this.notificationList.length > 0) {
        this.dropResponse = this.notificationList;
      } else {
        this.dropResponse = null;
      }
      if (this.selectedDropdown.value) {
        if (this.selectedDropdown.value === NOTFTOPICSCONST.ALLTOPICS) {
          this.dropResponse = this.notificationList;
        } else {
          this.dropResponse = this.notificationList.filter(p => p.topic === this.selectedDropdown.value);
        }
        if (this.dropResponse.length > 0) {
          setTimeout(() => {
            this.isHTPDeviceMSGTriggered(this.dropResponse);
            this.dataSource = new MatTableDataSource<PeriodicElement>(this.dropResponse);
            this.dataSource.sort = this.sort;
          }, 100);
        }
      }
      if (resp && resp.length > 0) {
        setTimeout(() => {
          this.setSelectedNT();
        }, 400);
      }
    } else {
      setTimeout(() => {
        this.dropResponse = null;
        this.dataSourceViewed = null;
        this.isHTPDeviceMSGTriggered(this.dropResponse);
        this.dataSource = new MatTableDataSource<PeriodicElement>(this.dropResponse);
        this.dataSource.sort = this.sort;
      }, 100);
    }
    this.selectDDByTopicValue();
  }

  getDropdownValue() {
    this.dropdownValue = this._notificationservice.getAllDropdowns().subscribe(resp => {
      this.dropdownResp = resp;
    }, errorData => {
      console.log('Error on getDropdownValue', errorData);
    });
  }

  onChangeTopic(event: any) {
    console.log(event);
    this.selectedDropdown = event;
    if (this.selectedTab !== 1) {
      if (event.value === NOTFTOPICSCONST.ALLTOPICS) {
        this.dropResponse = this.notificationList;
      } else {
        this.dropResponse = this.notificationList.filter((p) => {
          return p.topic === event.value;
        });
      }
    } else {
      if (event.value === NOTFTOPICSCONST.ALLTOPICS) {
        this.dropResponse = this.dataSourceViewed;
      } else {
        this.dropResponse = this.dataSourceViewed.filter((p) => {
          return p.topic === event.value;
        });
      }
    }

    this.isDesCarEnabled = false;
    this.selection.clear();
    this.rowData = null;
    this.isHTPDeviceMSGTriggered(this.dropResponse);
    this.dataSource = new MatTableDataSource<PeriodicElement>(this.dropResponse);
    this.dataSource.sort = this.sort;
    if (this.dropResponse.length > 0) {
      this.getRowData(this.dropResponse[0]);
    }
    setTimeout(() => {
      if (this.dropResponse.length > 0) {
        this.setScrollToTop();
      }
    }, 100);
  }

  getRowData(row) {
    this.isCheckBoxClicked = false;
    this.isrowSelectedRow = row;
    this.selection.clear();
    this.rowData = row;
    this.isDesCarEnabled = true;
    if (row.title !== HTPTRIGGEREDNF) {
      this.getCode(row);
    } else {
      this.selectedNotfication = row;
    }
  //  console.log(this.rowData, 'this.rowData');
  }

  isHTPDeviceMSGTriggered(res: any) {
    const HTPTNF = res.filter(elm => elm.title === HTPTRIGGEREDNF);
    if (HTPTNF && HTPTNF.length > 0) {
      if (res.length === HTPTNF.length) {
        this.isHTPDeviceMSGT = true;
      }
    } else {
      this.isHTPDeviceMSGT = false;
    }
  }

  onConfirming() {
    // tslint:disable-next-line:prefer-const
    let finalOutput = [];
    this.selection.selected.filter(res => finalOutput.push(res));
    console.log(finalOutput, 'finalOutput');
    this._notificationservice.updateNotification(finalOutput).subscribe(successData => {
      this._sharedSvc.setData('bulkordercsvUploadSuccess', successData);
      // tslint:disable-next-line:max-line-length
      this._snackBarSvc.showSuccessSnackBar(`Notification message confirmed successfully.`, SnackbarClasses.successBottom2);
      this.selection.clear();
      this.onLoadingNotificationList();
      this.header.getNotificationCount();
      this.rowData = null;
    }, errorData => {
      this._snackBarSvc.showErrorSnackBar(`Error occurred. Kindy re-try.`, SnackbarClasses.errorBottom2);
      console.log('Error on updating the messages.', errorData);
    });
  }

  public emptyCheck(value) {
    if (value === '' || !value) {
      return true;
    } else {
      return false;
    }
  }
}
