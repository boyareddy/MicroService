import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { XhrProgressLoaderComponent } from './xhr-progress-loader.component';

describe('XhrProgressLoaderComponent', () => {
  let component: XhrProgressLoaderComponent;
  let fixture: ComponentFixture<XhrProgressLoaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ XhrProgressLoaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(XhrProgressLoaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
