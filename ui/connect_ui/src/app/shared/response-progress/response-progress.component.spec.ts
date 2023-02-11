import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponseProgressComponent } from './response-progress.component';
import { MaterialModule } from 'src/app/shared/material.module';

describe('ResponseProgressComponent', () => {
  let component: ResponseProgressComponent;
  let fixture: ComponentFixture<ResponseProgressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MaterialModule
      ],
      declarations: [ ResponseProgressComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResponseProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
