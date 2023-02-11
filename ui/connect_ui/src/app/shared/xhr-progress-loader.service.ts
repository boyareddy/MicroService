import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material';
import { XhrProgressLoaderComponent } from '../shared/xhr-progress-loader/xhr-progress-loader.component';

@Injectable({
  providedIn: 'root'
})
export class XhrProgressLoaderService {

  progressInfo: any = {};
  // tslint:disable-next-line:no-inferrable-types
  progressCompleted: boolean = false;
  private closeSpinner: () => void;

  constructor(private _dialogBox: MatDialog) {
    // this.setProgressStatus(false);
    this.closeSpinner = () => this.closeProgress();
  }

  setBoundries(boundingClientRect, aditionalTop) {
    aditionalTop = aditionalTop  ? aditionalTop : 0;
    document.addEventListener('mousedown', this.closeSpinner, true);
    setTimeout(timeOut => {
      console.log('DDDDDDDDDDDDD');
      const backDrop = document.getElementsByClassName('cdk-overlay-container');
      if (backDrop && backDrop.length > 0) {
        const sprinerElm = (<HTMLElement>backDrop[0]);
        const spinnerCssProp = `.spinnerCssProp {
          width: ` + (boundingClientRect.width) + `px;
          top: ` + (boundingClientRect.top + aditionalTop) + `px;
          left: ` + boundingClientRect.left + `px;
          right: ` + boundingClientRect.right + `px;
        }`;
        const styleElm = (<HTMLElement>
          document.getElementsByTagName('style')[0]);
          styleElm.appendChild(document.createTextNode(spinnerCssProp));
          sprinerElm.setAttribute('class', 'cdk-overlay-container spinnerCssProp');
        // sprinerElm.style.outline = '1px solid red';
        // sprinerElm.style.width = (boundingClientRect.width - boundingClientRect.left) + 'px';
        // sprinerElm.style.top = boundingClientRect.top + 'px';
        // sprinerElm.style.left = boundingClientRect.left + 'px';
        // sprinerElm.style.right = boundingClientRect.right + 'px';
      }
    }, 100);
  }

  removeBounderies() {
    console.log('FFFFFFFFFFFFFF');
    document.removeEventListener('mousedown', this.closeSpinner, true);
    const backDrop = document.getElementsByClassName('cdk-overlay-container');
    if (backDrop && backDrop.length > 0) {
      const sprinerElm = (<HTMLElement>backDrop[0]);
      sprinerElm.setAttribute('class', 'cdk-overlay-container');
    }
  }


  getProgressInfo(xhrName: string) {
    return this.progressInfo[xhrName];
  }

  setProgressInfo(xhrName: string, progressStatus: boolean) {
    this.progressInfo[xhrName] = progressStatus;
  }

  setProgressStatus(progressCompleted) {
    this.progressCompleted = progressCompleted;
  }

  openProgress(): void {
    // tslint:disable-next-line:prefer-const
    let backDrpClass = 'progress-loader-no-bd';

    setTimeout(timeOut => {
      // tslint:disable-next-line:prefer-const
      let backDrop = document.getElementsByClassName('progress-loader-no-bd');
      // console.log(backDrop && backDrop.length > 0);
      if (backDrop && backDrop.length > 0) {
        (<HTMLElement>backDrop[0]).style.background = 'rgba(255,255,255, 0.75)';
      }
    }, 1000);

    this._dialogBox.open(XhrProgressLoaderComponent, {
      width: '486px',
      height: '200px',
      panelClass: 'progress-loader',
      backdropClass: backDrpClass,
      hasBackdrop: true,
      data: {onlyWarn: 'No changes have been made.'},
    }).afterClosed().subscribe(() => {
      this.removeBounderies();
      console.log('PPPPPPPPPPP');
    });
  }

  closeProgress(): void {
    // this.setProgressStatus(true);
      console.log('KKKKKKKKKKKKKK');
    this._dialogBox.closeAll();
    this.removeBounderies();
  }
}
