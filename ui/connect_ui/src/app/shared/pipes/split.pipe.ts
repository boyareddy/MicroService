import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'split'
})
export class SplitPipe implements PipeTransform {

  transform(value: any, args: string): any {
    let afterSplit: any;
    if (args && value) {
      afterSplit = value.split(args);
      return afterSplit;
    } else {
      return ['-'];
    }
  }

}
