export class SplittedArray {
    positive: any[] = [];
    negetive: any[] = [];
}

export default class ArrayUtil {
    static splitArrayList(arrayList: any[], key: string): SplittedArray {
        const resultArray = new SplittedArray();
        arrayList.forEach(element => {
            if (!element[key]) {
                resultArray.positive.push(element);
            } else {
                resultArray.negetive.push(element);
            }
        });
        return resultArray;
    }
}

export const removeEmpty = (array: any[]): any[] => {
    let resArray = array.filter(ele => { return ele.trim() !== "" });
    return resArray;
}

export const sort = (list: any[], key: string) => {
    let sortedList = list.sort((a,b) => (a[key].toLowerCase() > b[key].toLowerCase()) ? 1 : ((b[key].toLowerCase() > a[key].toLowerCase()) ? -1 : 0));
    return sortedList;
}