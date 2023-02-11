import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCenterEllipsesComponent } from './show-center-ellipses.component';
import { MatTooltipModule } from '@angular/material';

describe('ShowCenterEllipsesComponent', () => {
  let component: ShowCenterEllipsesComponent;
  let fixture: ComponentFixture<ShowCenterEllipsesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowCenterEllipsesComponent ],
      imports: [MatTooltipModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowCenterEllipsesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
