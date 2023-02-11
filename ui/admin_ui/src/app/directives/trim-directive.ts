import { Directive, HostListener } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[appTrim]'
})
export class TrimDirective {

    constructor(private formControl: NgControl) {}

    @HostListener('blur', ['$event'])
    onBlur($event) {
        this.trimInputStr ($event);
    }

    @HostListener('mousemove', ['$event'])
    onnMouseMove($event) {
        this.trimInputStr ($event);
    }

    public trimInputStr ($event) {
        const value_ = $event.target.value;
        const startSpace = /^\s/;
        const endSpace = /\s$/;
        if (endSpace.test(value_) || startSpace.test(value_)) {
            $event.target.value = value_.trim();
            this.formControl.control.setValue($event.target.value);
            this.formControl.control.updateValueAndValidity();
        }
    }
}
