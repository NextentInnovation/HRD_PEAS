import { SiteURLS } from '../../../core/constants/urls/site-url.constant';
import { DateUtils } from '../../../core/utils/date.util';
import { CommonUtils } from '../../../core/utils/common.util';
import { Injectable } from '@angular/core';
import { ModalOptions } from 'ngx-bootstrap';
import { ReferenceType, ViewMode } from '@src/app/core/constants/constans';
import { NotificationModel } from '@src/app/models/notification/notification.model';
import { Parameters } from '@src/app/core/utils/parameters.utils';

@Injectable()
export class BaseComponent {
  public SiteURLS = SiteURLS;
  public DateUtils = DateUtils;
  public CommonUtils = CommonUtils;
  public ViewMode = ViewMode;
  public Parameters = Parameters;

  public modalOptions: ModalOptions = {
    ignoreBackdropClick: true,
    backdrop: false,
    initialState: {}
  };

  calendarLocalConfig: any;

  constructor() {
    this.calendarLocalConfig = {
      firstDayOfWeek: 1,
      dayNames: ['Vasárnap', 'Hétfő', 'Kedd', 'Szerda', 'Csütörtök', 'Péntek', 'Szombat'],
      dayNamesShort: ['Vas', 'Hét', 'Ke', 'Sze', 'Csü', 'Pén', 'Szo'],
      dayNamesMin: ['Va', 'Hé', 'ke', 'Sz', 'Cs', 'Pé', 'Szo'],
      monthNames: [ 'Január', 'Február', 'Március', 'Április', 'Május', 'Június', 'Július', 'Augusztus',
        'Szeptember', 'Október', 'November', 'December' ],
      monthNamesShort: [ 'Jan', 'Feb', 'Már', 'Ápr', 'Máj', 'Jún', 'Júl', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ],
      today: 'Ma',
      clear: 'Töröl',
      dateFormat: 'yy-mm-dd',
      weekHeader: 'Hét'
    };
  }

  back() {
    window.history.back();
  }

}
