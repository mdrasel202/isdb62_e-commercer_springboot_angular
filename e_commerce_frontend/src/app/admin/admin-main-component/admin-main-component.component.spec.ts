import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminMainComponentComponent } from './admin-main-component.component';

describe('AdminMainComponentComponent', () => {
  let component: AdminMainComponentComponent;
  let fixture: ComponentFixture<AdminMainComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminMainComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminMainComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
