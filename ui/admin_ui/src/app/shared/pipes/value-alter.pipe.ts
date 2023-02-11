import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'valueAlter'
})
export class ValueAlterPipe implements PipeTransform {

  transform(value: any, fstArg?: string, sndArg?: string): any {
    return fstArg === sndArg ? '-' : value;
  }

}
