import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Ttv2analyzerComponent } from './ttv2analyzer.component';

describe('Ttv2analyzerComponent', () => {
  let component: Ttv2analyzerComponent;
  let fixture: ComponentFixture<Ttv2analyzerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Ttv2analyzerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Ttv2analyzerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
