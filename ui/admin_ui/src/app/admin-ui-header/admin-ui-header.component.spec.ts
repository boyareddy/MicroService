import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminUiHeaderComponent } from './admin-ui-header.component';
import { MatCardModule, MatToolbarModule, MatMenuModule} from '@angular/material';
import { MdePopoverModule } from '@material-extended/mde';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';

describe('AdminUiHeaderComponent', () => {
  let component: AdminUiHeaderComponent;
  let fixture: ComponentFixture<AdminUiHeaderComponent>;
  const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatCardModule,
        MatToolbarModule,
        MatMenuModule,
        MdePopoverModule,
        RouterTestingModule
      ],
      declarations: [ AdminUiHeaderComponent],
      providers: [
        { provide: Router, useValue: routerSpy }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUiHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onNavigatingToAddUser', () => {
    const _routerSpy = routerSpy.navigate as jasmine.Spy;
    fixture.detectChanges();
    fixture.componentInstance.onNavigatingToAddUser();
    expect (_routerSpy).toHaveBeenCalledWith(['add-user']);
  });

  // it('onNavigate', () => {
  //   component.onNavigate('test-url');
  //   //expect(window.location.href).toEqual('http://localhost:9876/context.html');
  // });
});
