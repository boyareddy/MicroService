import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowMoreMultilineComponent } from './show-more-multiline.component';

describe('ShowMoreMultilineComponent', () => {
  let component: ShowMoreMultilineComponent;
  let fixture: ComponentFixture<ShowMoreMultilineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowMoreMultilineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowMoreMultilineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
