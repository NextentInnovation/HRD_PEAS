export class TableLocalSorterControl {
  private sortedcol: string;
  private sortedtype: boolean;
  private _basesorttypeasc: boolean;
  
  private lastSortColIsNum: boolean;
  /*
   static sorting(obj: any, sortname: string, asc: boolean): any {
   let isNum = false;
   if (sortname.indexOf('.') >= 0) {
   const names: string[] = sortname.split('.');
   switch (names.length) {
   case 2:
   isNum = typeof (obj[0][names[0]][names[1]]) === 'number';
   return obj.sort(this.dynamic2Sort(names[0], names[1], asc, isNum));
   case 3:
   isNum = typeof (obj[0][names[0]][names[1]][names[2]]) === 'number';
   return obj.sort(this.dynamic3Sort(names[0], names[1], names[2], asc, isNum));
   case 4:
   isNum = typeof (obj[0][names[0]][names[1]][names[2]][names[3]]) === 'number';
   return obj.sort(this.dynamic4Sort(names[0], names[1], names[2], names[3], asc, isNum));
   default:
   break;
   }
   } else {
   isNum = typeof (obj[0][sortname]) === 'number';
   return obj.sort(this.dynamicSort(sortname, asc, isNum));
   }
   }
   */
  private static baseSort(a, b, isNum: boolean): number {
    if (a === null && b === null) {
      return 0;
    }
    if (a === null) {
      return 1;
    }
    if (b === null) {
      return -1;
    }
    if (isNum) {
      // console.log('baseSort: isNum: true ' + a + ' ' + b);
      return (a < b) ? -1 : (a > b) ? 1 : 0;
    } else {
      // console.log('baseSort: isNum: false ' + a + ' ' + b);
      return a.toString().localeCompare(b.toString());
    }
  }
  
  private static dynamicSort(sortname: string, asc: boolean, isNum: boolean) {
    const sortOrder = asc ? 1 : -1;
    return function (a, b) {
      const result = TableLocalSorterControl.baseSort(a[sortname], b[sortname], isNum);
      return result * sortOrder;
    };
  }
  
  private static dynamic2Sort(name1: string, name2: string, asc: boolean, isNum: boolean) {
    const sortOrder = asc ? 1 : -1;
    return function (a, b) {
      const result = TableLocalSorterControl.baseSort(a[name1][name2], b[name1][name2], isNum);
      return result * sortOrder;
    };
  }
  
  private static dynamic3Sort(name1: string, name2: string, name3: string, asc: boolean, isNum: boolean) {
    const sortOrder = asc ? 1 : -1;
    return function (a, b) {
      const result = TableLocalSorterControl.baseSort(a[name1][name2][name3], b[name1][name2][name3], isNum);
      return result * sortOrder;
    };
  }
  
  private static dynamic4Sort(name1: string, name2: string, name3: string, name4: string, asc: boolean, isNum: boolean) {
    const sortOrder = asc ? 1 : -1;
    return function (a, b) {
      const result = TableLocalSorterControl.baseSort(a[name1][name2][name3][name4], b[name1][name2][name3][name4], isNum);
      return result * sortOrder;
    };
  }
  
  constructor(basesorttypeASC?: boolean) {
    if ( basesorttypeASC === null || basesorttypeASC === undefined ) {
      this.basesorttypeasc = true;
    } else {
      this.basesorttypeasc = basesorttypeASC;
    }
    // console.log('[Sort][CONSTRUCTOR] :: basesorttypeASC => ' + basesorttypeASC + ' | _basesorttypeasc => ' + this._basesorttypeasc );
  }
  // TODO: Rendet teremnteni a kaoszban!
  sorting(obj: any, sortname: string ) {
    let lastSortedCol = false;
    // console.log('[START SORTING] this.sortedcol: ' + this.sortedcol + ' this.sortedtype: ' + this.sortedtype);
    if (sortname.localeCompare(this.sortedcol) === 0) {
      this.sortedtype = this.sortedtype ? false : true;
      lastSortedCol = true;
      // console.log('[SAME SORTING] this.sortedcol: ' + this.sortedcol + ' this.sortedtype: ' + this.sortedtype);
    } else {
      this.sortedcol = sortname;
      this.sortedtype = this._basesorttypeasc;
      // console.log('[NEW SORTING] this.sortedcol: ' + this.sortedcol + ' this.sortedtype: ' + this.sortedtype);
    }
    // console.log('[END SORTING]');
    let isNum = false;
    if (sortname.indexOf('.') >= 0) {
      const names: string[] = sortname.split('.');
      switch (names.length) {
        case 2:
          if ( lastSortedCol ) {
            isNum = this.lastSortColIsNum;
          } else {
            isNum = typeof (obj[0][names[0]][names[1]]) === 'number';
            this.lastSortColIsNum = isNum;
          }
          // console.log('[2 - sorting]: lastSortedCol: ' + lastSortedCol + ' isNum: ' + isNum + ' lastSortColIsNum: ' + this.lastSortColIsNum);
          return obj.sort(TableLocalSorterControl.dynamic2Sort(names[0], names[1], this.sortedtype, isNum));
        case 3:
          if ( lastSortedCol ) {
            isNum = this.lastSortColIsNum;
          } else {
            isNum = typeof (obj[0][names[0]][names[1]]) === 'number';
            this.lastSortColIsNum = isNum;
          }
          // console.log('[3 - sorting]: lastSortedCol: ' + lastSortedCol + ' isNum: ' + isNum + ' lastSortColIsNum: ' + this.lastSortColIsNum);
          return obj.sort(TableLocalSorterControl.dynamic3Sort(names[0], names[1], names[2], this.sortedtype, isNum));
        case 4:
          if ( lastSortedCol ) {
            isNum = this.lastSortColIsNum;
          } else {
            isNum = typeof (obj[0][names[0]][names[1]]) === 'number';
            this.lastSortColIsNum = isNum;
          }
          // console.log('[4 - sorting]: lastSortedCol: ' + lastSortedCol + ' isNum: ' + isNum + ' lastSortColIsNum: ' + this.lastSortColIsNum);
          return obj.sort(TableLocalSorterControl.dynamic4Sort(names[0], names[1], names[2], names[3], this.sortedtype, isNum));
        default:
          break;
      }
    } else {
      isNum = typeof (obj[0][sortname]) === 'number';
      return obj.sort(TableLocalSorterControl.dynamicSort(sortname, this.sortedtype, isNum));
    }
  }
  
  set basesorttypeasc(value: boolean) {
    this._basesorttypeasc = value;
  }
}
