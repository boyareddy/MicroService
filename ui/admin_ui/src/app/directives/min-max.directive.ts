import { Directive, ElementRef, HostListener, Input } from '@angular/core';
import { SnackBarService } from '../services/snack-bar.service';

@Directive({
  selector: '[appMinMax]'
})
export class MinMaxDirective {

  @Input() parentField: any;
  constructor(private element: ElementRef,  private _snackBarSvc: SnackBarService) { }

  @HostListener('keydown', ['$event']) onKeyDown($event) {
    const matErrors = this.parentField._elementRef.nativeElement.getElementsByTagName('mat-error');
    const maxLength = this.element.nativeElement.maxLength;
    const fieldLabel = this.parentField._elementRef.nativeElement.getElementsByTagName('mat-label');
    const matFormFieldRipple = this.parentField._elementRef.nativeElement.getElementsByClassName('mat-form-field-ripple');
    /* console.log('hello'); */
    if (this.element.nativeElement.type !== 'number') {
    this.checkMessage(matErrors, maxLength, fieldLabel, matFormFieldRipple, $event);
  }
  }

  @HostListener('blur') onBlur() {
    const errors = this.parentField._elementRef.nativeElement.getElementsByClassName('app-min-max-dir');
    const fieldLabel = this.parentField._elementRef.nativeElement.getElementsByTagName('mat-label');
    const matFormFieldRipple = this.parentField._elementRef.nativeElement.getElementsByClassName('mat-form-field-ripple');
    if (errors.length > 0) {
      this.parentField._elementRef.nativeElement.removeChild(errors[0]);
      fieldLabel[0].classList.remove('invalid-field');
      matFormFieldRipple[0].classList.remove('invalid-mat-form-field-ripple');
    }
  }

  @HostListener('keypress', ['$event']) onkeypress($event) {
    if (this.element.nativeElement.type === 'number') {
      const key = $event.key;
      if (key === 'e' || key === 'E' || key === '.' || key === '+' || key === '-') {
        $event.preventDefault();
      }
    }
  }

  @HostListener('paste', ['$event']) onpaste($event) {
    if (this.element.nativeElement.type === 'number') {
      const maxLength = this.element.nativeElement.maxLength;
      this.removeData(maxLength, $event);
      const val = $event.clipboardData.getData('text/plain');
      if (val.indexOf('e') > -1 || val.indexOf('.') > -1 || val.indexOf('+') > -1 || val.indexOf('-') > -1) {
        $event.preventDefault();
      }
    } else {
      this.showHideError($event);
    }
  }

   @HostListener('input', ['$event']) oninput($event) {
    const maxLength = this.element.nativeElement.maxLength;
    if (this.element.nativeElement.type === 'number') {
      this.removeData(maxLength, $event);
    }
    const matErrors = this.parentField._elementRef.nativeElement.getElementsByTagName('mat-error');
    const fieldLabel = this.parentField._elementRef.nativeElement.getElementsByTagName('mat-label');
    const matFormFieldRipple = this.parentField._elementRef.nativeElement.getElementsByClassName('mat-form-field-ripple');
    // console.log('hello');
    // this.checkMessage(matErrors, maxLength, fieldLabel, matFormFieldRipple, $event);

    if (this.element.nativeElement.type === 'number') {
    if (matErrors.length === 0) {
      if (this.element.nativeElement.value.length > maxLength && $event.keyCode !== 8) {
        const errors = this.parentField._elementRef.nativeElement.getElementsByClassName('app-min-max-dir');
        if (errors.length > 0) {
          this.parentField._elementRef.nativeElement.removeChild(errors[0]);
        }
        const errorToBeAppended = document.createElement('div');
        fieldLabel[0].classList.add('invalid-field');
        matFormFieldRipple[0].classList.add('invalid-mat-form-field-ripple');
        errorToBeAppended.classList.add('app-min-max-dir');
        const maxLengthVal = maxLength ? maxLength : 30;
          const translateMsg = this._snackBarSvc.getMessage('orders.maxLength').replace('$$$', maxLengthVal);
        errorToBeAppended.innerHTML = translateMsg;
        this.parentField._elementRef.nativeElement.appendChild(errorToBeAppended);
        console.log('Error');
      } else {
        this.showHideError($event);
      }
    } else {
      this.showHideError($event);
    }
  } else {
    this.showHideError($event);
  }
  }

  removeData(maxLength, $event) {
    const a = ($event.target.value) + '';
    if (a.length >= maxLength) {
      const object = $event.target;
      object.value = object.value.slice(0, object.maxLength);
    }
  }

  checkMessage(matErrors, maxLength, fieldLabel, matFormFieldRipple, $event) {
    console.log(this.element.nativeElement.value.length);
    if (matErrors.length === 0) {
      if (this.element.nativeElement.value.length >= maxLength && $event.keyCode !== 8) {
        const errors = this.parentField._elementRef.nativeElement.getElementsByClassName('app-min-max-dir');
        if (errors.length > 0) {
          this.parentField._elementRef.nativeElement.removeChild(errors[0]);
        }
        const errorToBeAppended = document.createElement('div');
        fieldLabel[0].classList.add('invalid-field');
        matFormFieldRipple[0].classList.add('invalid-mat-form-field-ripple');
        errorToBeAppended.classList.add('app-min-max-dir');
        const maxLengthVal = maxLength ? maxLength : 30;
          const translateMsg = this._snackBarSvc.getMessage('orders.maxLength').replace('$$$', maxLengthVal);
        errorToBeAppended.innerHTML = translateMsg;
        this.parentField._elementRef.nativeElement.appendChild(errorToBeAppended);
        console.log('Error');
      } else {
        this.showHideError($event);
      }
    } else {
      this.showHideError($event);
    }
  }

  showHideError($event) {
    const matFormFieldRipple = this.parentField._elementRef.nativeElement.getElementsByClassName('mat-form-field-ripple');

    const fieldLabel = this.parentField._elementRef.nativeElement.getElementsByTagName('mat-label');
    const errors = this.parentField._elementRef.nativeElement.getElementsByClassName('app-min-max-dir');
    if (errors.length > 0) {
      this.parentField._elementRef.nativeElement.removeChild(errors[0]);
      fieldLabel[0].classList.remove('invalid-field');
      matFormFieldRipple[0].classList.remove('invalid-mat-form-field-ripple');
    }
  }

}
