import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeMainLayoutComponent } from './home-main-layout.component';

describe('HomeMainLayoutComponent', () => {
  let component: HomeMainLayoutComponent;
  let fixture: ComponentFixture<HomeMainLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeMainLayoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeMainLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
