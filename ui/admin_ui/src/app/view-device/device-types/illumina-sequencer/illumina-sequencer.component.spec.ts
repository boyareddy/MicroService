import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IlluminaSequencerComponent } from './illumina-sequencer.component';

describe('IlluminaSequencerComponent', () => {
  let component: IlluminaSequencerComponent;
  let fixture: ComponentFixture<IlluminaSequencerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IlluminaSequencerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IlluminaSequencerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
