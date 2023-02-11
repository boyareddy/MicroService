import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'emptyDataCheck'})
export class EmptyDataCheck implements PipeTransform {
  transform(value: any): any {
    if (value === '' || !value) {
      return '-';
    } else {
      return value;
    }
  }
}
