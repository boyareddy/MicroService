import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'pluralCheck'
})
export class PluralCheckPipe implements PipeTransform {

  transform(value: any, count: any ): any {
    return parseInt(count, 10) === 1 ? value : `${value}s`;
  }

}
