import { NativeDateAdapter, DateAdapter, MAT_DATE_FORMATS, MatDateFormats } from '@angular/material';


export class AppDateAdapter extends NativeDateAdapter {
   format(date: Date, displayFormat: string): string {
       console.log(date.getMonth(), 'date.getMonth()-------------------');
       if (displayFormat === 'input') {
          const day = date.getDate();
          const month = date.getMonth() + 1;
          const year = date.getFullYear();
          return  this._to2digit(month) + '/' + this._to2digit(day) + '/' + year;
       } else if (displayFormat === 'inputMonth') {
          const month = date.getMonth() + 1;
          const year = date.getFullYear();
          return  this._to2digit(month) + '/' + year;
       } else {
           return date.toDateString();
       }
   }

   private _to2digit(n: number) {
       return ('00' + n).slice(-2);
   }
}

export const APP_DATE_FORMATS = {
   parse: {
       dateInput: {month: 'short', year: 'numeric', day: 'numeric'}
   },
   display: {
       // dateInput: { month: 'short', year: 'numeric', day: 'numeric' },
       dateInput: 'input',
       // monthYearLabel: { month: 'short', year: 'numeric', day: 'numeric' },
       monthYearLabel: 'inputMonth',
       dateA11yLabel: {year: 'numeric', month: 'long', day: 'numeric'},
       monthYearA11yLabel: {year: 'numeric', month: 'long'},
   }
};
