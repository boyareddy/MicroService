import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BackupandrestoreComponent } from './backupandrestore.component';

describe('BackupandrestoreComponent', () => {
  let component: BackupandrestoreComponent;
  let fixture: ComponentFixture<BackupandrestoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BackupandrestoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BackupandrestoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
