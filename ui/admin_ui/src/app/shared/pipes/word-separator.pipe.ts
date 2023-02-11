import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'wordSeparator'
})
export class WordSeparatorPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    let newWord = value ? value.join(', ') : '';
    return newWord;
  }

}
