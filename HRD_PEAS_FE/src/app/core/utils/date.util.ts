import { Injectable } from '@angular/core';
import { CommonUtils } from './common.util';

@Injectable()
export class DateUtils {

  public static DATE_FORMAT = 'y-MM-dd';
  public static DATE_TIME_FORMAT = 'y.MM.dd. HH:mm';

  public static yearRange(nowMinus?: number, nowPlus?: number): string {
    let ret = '';
    const nowPlusOneYear = (new Date().getFullYear() + 1).toString();
    if (CommonUtils.isEmpty(nowMinus) && CommonUtils.isEmpty(nowPlus)) {
      ret = '2018:' + nowPlusOneYear;
    } else {
      let min = '2018';
      let max = nowPlusOneYear;
      if (CommonUtils.isNotEmpty(nowMinus)) {
        min = (new Date().getFullYear() - nowMinus).toString();
      }
      if (CommonUtils.isNotEmpty(nowPlus)) {
        max = (new Date().getFullYear() + nowPlus).toString();
      }
      ret = min + ':' + nowPlusOneYear;
    }
    return ret;
  }

  public static formatHours(date: string): string {
    if (date && date.includes(' ')) {
      const dateFragments = date.split( ' ' );
      let ret = dateFragments[ 0 ];
      ret += ' <span class="date-hour">' + dateFragments[ 1 ] + '</span>';
      return ret;
    }
    return '';
  }

  public static setEndOfDay(date: Date): Date {
    if (date) {
      date.setHours( 23, 59, 59, 0 );
    }
    return date;
  }

  public static setStartOfDay(date: Date): Date {
    if (date) {
      date.setHours( 0, 0, 0, 0 );
    }
    return date;
  }
}
