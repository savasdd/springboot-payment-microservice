import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCityComponent } from './user-city.component';

describe('UserCityComponent', () => {
  let component: UserCityComponent;
  let fixture: ComponentFixture<UserCityComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserCityComponent]
    });
    fixture = TestBed.createComponent(UserCityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
