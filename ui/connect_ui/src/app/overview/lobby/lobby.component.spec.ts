import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LobbyComponent } from './lobby.component';
import { HeaderComponent } from 'src/app/shared/header/header.component';
import { IconComponent } from '../icon/icon.component';

fdescribe('LobbyComponent', () => {
  let component: LobbyComponent;
  let fixture: ComponentFixture<LobbyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LobbyComponent, HeaderComponent, IconComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LobbyComponent);
    component = fixture.componentInstance;
    const roleInfo = {
      roleID: 1,
      roleInfo: 'Admin'
    };
    sessionStorage.setItem('usersInfo', JSON.stringify(roleInfo));
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
