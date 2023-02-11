/**
 * appShowMore is the directive which is used to hide some part of a text and while user mouse over on the
 * label text, it shows a tooltip of full text.
 * It can be used as below,
 * <div class="label_value" [matTooltip] = "item.comments?.length > 27?item.comments:null" appShowMore>
      {{ item.comments }}
   </div>
 * we have to use matTooltip along with appShowMore if it is nessasary to display the tooltip.
 */

import { Directive, ElementRef, OnInit, AfterViewInit, HostListener, Input, HostBinding, Renderer } from '@angular/core';
import { MatTooltip } from '@angular/material';

@Directive({
  selector: '[appShowMore]'
})
export class ShowMoreDirective implements AfterViewInit {

  constructor(private _elementRef: ElementRef) { }

  ngAfterViewInit() {
    this.hideMore();
  }

  /**
   * hideMore hides the text > 27
   */
  hideMore() {
    console.log(this._elementRef);
    const element = this._elementRef.nativeElement;
    if (element.innerHTML && element.innerHTML.length > 27) {
      this._elementRef.nativeElement.innerHTML = element.innerHTML.substr(0, 27).concat('...');
      this._elementRef.nativeElement.style.width = '250px';
    }
  }

}
