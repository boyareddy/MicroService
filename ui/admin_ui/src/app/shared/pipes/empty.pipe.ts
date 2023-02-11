import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'empty'
})
export class EmptyPipe implements PipeTransform {

  transform(value: any, replaceChar?: any): any {
    return value && value.trim() !== '' ? value : replaceChar;
  }

}
