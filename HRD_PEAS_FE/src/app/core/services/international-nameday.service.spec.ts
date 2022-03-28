import { TestBed } from '@angular/core/testing';

import { InternationalNamedayService } from './international-nameday.service';

describe('InternationalNamedayService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: InternationalNamedayService = TestBed.get(InternationalNamedayService);
    expect(service).toBeTruthy();
  });
});
