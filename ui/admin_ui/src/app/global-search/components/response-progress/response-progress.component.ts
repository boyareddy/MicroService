import { Component, OnInit, Input, OnChanges, AfterViewInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-response-progress',
  templateUrl: './response-progress.component.html',
  styleUrls: ['./response-progress.component.scss']
})
export class ResponseProgressComponent implements OnInit, OnChanges, AfterViewInit, OnDestroy {

  @Input() diameterValue;
  @Input() marginTop;
  @Input() rectPro;
  @Input() spOverlayWidth;
  @Input() spOverlayHeight;
  @Input() spOverlayTop;
  @Input() spOverlayLeft;
  @Input() spOverlayRight;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    const backDrop = document.getElementsByTagName('app-response-progress');
    if (backDrop && backDrop.length > 0) {
      document.body.appendChild((<HTMLElement>backDrop[0]));
    }
  }

  ngOnDestroy() {
    const backDrop = document.getElementsByTagName('app-response-progress');
    if (backDrop && backDrop.length > 0) {
      (<HTMLElement>backDrop[0]).remove();
    }
  }

  ngOnChanges() {
    const backDrop = document.getElementsByClassName('spinner_overlay');
    if (backDrop && backDrop.length > 0) {
      if (this.rectPro) {
        console.log(this.rectPro.width, 'this.rectPro.width ###############');
        this.spOverlayWidth = this.spOverlayWidth  ? this.spOverlayWidth : this.rectPro.width;
        this.spOverlayTop = this.spOverlayTop  ? this.spOverlayTop : this.rectPro.top;
        this.spOverlayLeft = this.spOverlayLeft  ? this.spOverlayLeft : this.rectPro.left;
        this.spOverlayRight = this.spOverlayRight  ? this.spOverlayRight : this.rectPro.right;
      }
      const sprinerElm = (<HTMLElement>backDrop[0]);
      // const spinnerCssProp = `.spinnerCssProp {
      //   width: ` + (this.spOverlayWidth) + `px !important;
      //   height: ` + (this.spOverlayHeight) + `px !important;
      //   top: ` + this.spOverlayTop + `px !important;
      //   left: ` + this.spOverlayLeft + `px !important;
      //   right: ` + this.spOverlayRight + `px !important;
      // }`;
      // const styleElm = (<HTMLElement>
      //   document.getElementsByTagName('style')[0]);
      //   styleElm.appendChild(document.createTextNode(spinnerCssProp));
      //   sprinerElm.setAttribute('class', 'spinner_overlay spinnerCssProp');

        sprinerElm.style.width = this.spOverlayWidth + 'px';
        console.log(this.spOverlayHeight, 'this.spOverlayHeight----------');
        if (this.spOverlayHeight !== false) {
          this.spOverlayHeight = this.spOverlayHeight  ? this.spOverlayHeight : this.rectPro.height;
          sprinerElm.style.height = this.spOverlayHeight + 'px';
        }
        sprinerElm.style.top = this.spOverlayTop + 'px';
        sprinerElm.style.left =  this.spOverlayLeft + 'px';
        sprinerElm.style.right = this.spOverlayRight + 'px';
    }
  }
}
