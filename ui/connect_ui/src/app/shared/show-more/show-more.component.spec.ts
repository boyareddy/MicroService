import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowMoreComponent } from './show-more.component';
import { MatTooltipModule } from '@angular/material';
import { TooltipModule } from 'ng2-tooltip-directive';

describe('ShowMoreComponent', () => {
  let component: ShowMoreComponent;
  let fixture: ComponentFixture<ShowMoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowMoreComponent ],
      imports: [ MatTooltipModule, TooltipModule ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowMoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
