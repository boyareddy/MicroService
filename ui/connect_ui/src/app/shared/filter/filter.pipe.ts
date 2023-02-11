import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment-timezone';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(value: any, args?: any, dateKey?: string, commentKey?: string, flagKey?: string, mandatoryFieldMissing?: string): any {
    //console.log("filter", value, args);
    if(!args || Object.keys(args).length === 0){
      return value;
    }else{
      return this.getFilteredList(value, args, dateKey, commentKey, flagKey,mandatoryFieldMissing);
    }
  }

  getFilteredList(list: any[], filterOptions, dateKey?: string, comments?: string, flags?: string, mandatoryFieldMissing?: string): any[]{
    let checkboxKeys: string[] = ["comments", "flags","mandatoryFieldMissing"];
    let dateTimeKey = dateKey ? dateKey : 'runCompletionTime';

    if(!list){
      return [];
    }

    return list.filter(item => {
      for(let option in filterOptions){
        if(option === 'comments' && !item[option]){
          if(comments && !item[comments]){
            return null;
          }
          if(item.sampleResults){
            let sampleComments = item.sampleResults.filter(sample => { if(sample[option]){ return item; }})
            if(sampleComments.length === 0){ return null; }
          }
        }else if(option === 'mandatoryFieldMissing' && !item[option]){
            return null;
        }else if(option === 'flags' && !item.runFlag){
          if(flags && !item[flags]){
            return null;
          }
          if(item.sampleResults){
            let sampleComments = item.sampleResults.filter(sample => { if(sample.flag){ return item; }})
            if(sampleComments.length === 0){ return null; }
          }
        }else if(option === 'dateFrom'){
          let runCompletionTimeStamp = Date.parse(item[dateTimeKey]);
          let fromTimeStamp = Date.parse(filterOptions[option]);
          if(fromTimeStamp > runCompletionTimeStamp){
            return null;
          }
        }else if(option === 'dateTo'){
          let toDate = new Date(filterOptions[option]);
          let runCompletionTimeStamp = Date.parse(item[dateTimeKey]);
          let fromTimeStamp = new Date(new Date(toDate.setHours(23)).setMinutes(59)).setSeconds(59);
          if(fromTimeStamp < runCompletionTimeStamp){
            return null;
          }
        }else if(option === 'sampleType'){
          if(item.sampleResults){
            let sampleTypes = item.sampleResults.filter(sample => { if(sample.sampleType.toLowerCase() === sample[option].toLowerCase()){ return item; }})
            if(sampleTypes.length === 0){ return null; }
          }
        }else if(typeof filterOptions[option] !== 'boolean' && filterOptions[option] && item[option] && filterOptions[option].toLowerCase() !== item[option].toLowerCase()){
          return null;
        }else if(typeof filterOptions[option] !== 'boolean' && filterOptions[option] !== item[option]){
          return null;
        }
      }
      return item;
    });
  }
}
