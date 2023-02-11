import { Directive, Input, ElementRef, AfterViewInit } from '@angular/core';

@Directive({
  selector: '[appHighlight]'
})
export class HighlightDirective implements AfterViewInit{

  @Input() text: string;
  @Input() matchedText: string;

  constructor(private _ele: ElementRef) { }

  ngAfterViewInit(){
    let index = this.text.toLowerCase().indexOf(this.matchedText.toLowerCase());
    let finalHtml = this.text.substring(0,index) + "<span style='background: rgba(0, 78, 163, 0.2);'>" + this.text.substring(index, index + this.matchedText.length) + "</span>" + this.text.substring(index + this.matchedText.length);

    this._ele.nativeElement.innerHTML = finalHtml;
  }

}
