import { CommonUtils } from '../../utils/common.util';

export class TableSearchControl {
  
  static searchWidthObj(objs: object[], searchObj: object): object[] {
    let newObjs: object[] = objs;
    const serachList = Object.keys(searchObj);
    serachList.forEach(value => {
      newObjs = this.search(newObjs, value, searchObj[value]);
    });
    return newObjs;
  }
  
  static search(objs: object[], where: string, what: string, exact: boolean = false): object[] {
    // console.log(objs, where, what);
    let newObjs = new Array<object>();
    if (CommonUtils.isEmpty(what)) {
      newObjs = objs;
    } else {
      if (objs && objs.length > 0) {
        if (where.includes('.')) {
          objs.forEach(obj => {
            const keys = where.split('.');
            const value = this.getObjectValue(obj, keys);
            if (value && ((exact && value === what) || (!exact && value.toString().toLocaleLowerCase().includes(what.toLocaleLowerCase())))) {
              newObjs.push(obj);
            }
          });
        } else {
          objs.forEach(obj => {
            const value = this.getSerachObjectValue(obj, where);
            if (value && ((exact && value === what) || (!exact && value.toString().toLocaleLowerCase().includes(what.toLocaleLowerCase())))) {
              newObjs.push(obj);
            }
          });
        }
      }
    }
    return newObjs;
  }
  
  static getObjectValue(obj: object, keyList: string[]): string {
    let str = null;
    if (keyList.length === 1) {
      str = obj[keyList[0]];
    } else {
      const key = keyList[0];
      keyList = keyList.splice(0, 1);
      str = this.getObjectValue(obj[key], keyList);
    }
    return str;
  }
  
  static getSerachObjectValue(obj: object, where: string): string {
    let str = null;
    if (obj && CommonUtils.isNotEmpty(obj)) {
      const objKeys = Object.keys(obj);
      if (objKeys && objKeys.length > 0) {
        objKeys.forEach(value => {
          if (where === value) {
            str = obj[value];
          }
        });
        if (str === null) {
          let length = objKeys.length - 1;
          do {
            if (obj[objKeys[length]] !== obj && CommonUtils.isNotEmpty(obj[objKeys[length]])) {
              str = this.getSerachObjectValue(obj[objKeys[length]], where);
            }
            length--;
          } while (str === null && length >= 0);
        }
      }
    }
    return str;
  }
}
