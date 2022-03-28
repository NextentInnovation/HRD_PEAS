import { Injectable } from '@angular/core';
import * as moment from 'moment';

@Injectable()
export class CommonUtils {
  public static getBaseUrl( url: string ) {
    if ( url.startsWith( '/' ) ) {
      url = url.substring( 1 );
      url = url.split( '?' )[ 0 ];
    }
    return url;
  }

  public static isEmpty( element: string | number | any ): boolean {
    return ((element === undefined)
      || (element === 'undefined')
      || (element === null)
      || (element === '')
      || (element === 'null')
      || (element === 'NaN')
      || (typeof element === 'number' && Number.isNaN( element ))
    );
  }

  public static isNotEmpty( element: string | number | any ): boolean {
    return !CommonUtils.isEmpty( element );
  }

  /**
   * <select> HTML compareWith függvény, mely objektumokat ID alapján hasonlít össze.
   * @param a
   * @param b
   * @return true ha az ID egyenlő
   */
  public static compareEqualsById = ( a, b ) => {
    //  return (a && b && ((a.id === b.id) || (CommonUtils.isEmpty( a.id ) && CommonUtils.isEmpty( b.id ))));
    return CommonUtils.isNotEmpty( a ) && CommonUtils.isNotEmpty( b ) ? a.id === b.id : a === b;
  }

  public static compareEqualsObjectById = ( a, b ) => {
    console.log(a, b, CommonUtils.isNotEmpty( a ) && CommonUtils.isNotEmpty( b ) ? a.id === b : a === b);
    return CommonUtils.isNotEmpty( a ) && CommonUtils.isNotEmpty( b ) ? a.id === b : a === b;
  }

  public static deleteItemFromList( list: Array<any>, item: any) {
    const index = list.indexOf(item);
    if (index >= 0) { list.splice(index, 1); }
  }

  public static setParamsWithOtherObject<T>( settingObj: T, setterObj: object): T {
    Object.keys(setterObj).forEach( key => {
      const value = setterObj [ key ];
      if (Array.isArray(value)) {
        for (let i = 0; i < value.length; i++) {
          value[i] = this.ifDateStringConvertToDateObject(value[i]);
        }
        settingObj [ key ] = value;
      } else if (typeof value === 'object') {
        settingObj [ key ] = this.setParamsWithOtherObject(settingObj [ key ], value);
      } else {
        settingObj [ key ] = this.ifDateStringConvertToDateObject( value );
      }
    });
    return settingObj;
  }

  public static ifDateStringConvertToDateObject(value: any): Date | any {
    if ( moment( value, moment.ISO_8601, true ).isValid() ) {
      const newDate = new Date( value );
      return newDate;
    }
    return value;
  }
}
