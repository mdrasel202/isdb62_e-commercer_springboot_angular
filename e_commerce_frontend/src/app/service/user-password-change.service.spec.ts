import { TestBed } from '@angular/core/testing';

import { UserPasswordChangeService } from './user-password-change.service';

describe('UserPasswordChangeService', () => {
  let service: UserPasswordChangeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserPasswordChangeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
