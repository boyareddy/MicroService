import { MinMaxDirective } from './min-max.directive';
import { ElementRef, Component, DebugElement } from '@angular/core';
import { async, TestBed, ComponentFixture } from '@angular/core/testing';

export class MockElementRef extends ElementRef {
  nativeElement = {};
}

@Component({
  selector: 'app-test',
  template: `<div #accessioningId>
  <input type="text" appMinMax [parentField]="accessioningId">
  </div>`
})
class TestHoverFocusComponent {
}

describe('MinMaxDirective', () => {
  let component: TestHoverFocusComponent;
  let fixture: ComponentFixture<TestHoverFocusComponent>;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestHoverFocusComponent, MinMaxDirective]
    });
    fixture = TestBed.createComponent(TestHoverFocusComponent);
    component = fixture.componentInstance;
  });
  it('should create an instance', () => {
    const directive = new MinMaxDirective(null);
    expect(directive).toBeTruthy();
  });
  // it('should onKeyDown', () => {
  //   let elm_: ElementRef = fixture.debugElement;
  //   const directive = new MinMaxDirective(elm_);
  //   const event = { target: { value: 'xyz' } };
  //   directive.parentField = elm_;
  //   directive.onKeyDown(event);
  //   expect(event.target.value).toEqual('xyz');
  // });
});
