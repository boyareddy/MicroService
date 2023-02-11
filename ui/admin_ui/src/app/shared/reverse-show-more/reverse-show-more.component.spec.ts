import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReverseShowMoreComponent } from './reverse-show-more.component';

describe('ReverseShowMoreComponent', () => {
  let component: ReverseShowMoreComponent;
  let fixture: ComponentFixture<ReverseShowMoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReverseShowMoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReverseShowMoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
