import { TrimDirective } from './trim-directive';
import { TestBed } from '@angular/core/testing';
import { NgControl, FormControl } from '@angular/forms';

describe('TrimDirective', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrimDirective, NgControl]
    });
  });
  it('should create instance', () => {
    const directive = new TrimDirective(null);
    expect(directive).toBeTruthy();
  });
  it('should onnMouseMove', () => {
    const directive = new TrimDirective(null);
    const control = new FormControl('old value');
    const event = { target: { value: 'xyz' } };
    directive.onnMouseMove(event);
    expect(event.target.value).toEqual('xyz');
  });
  it('should onBlur', () => {
    const directive = new TrimDirective(null);
    const control = new FormControl('old value');
    const event = { target: { value: 'xyz' } };
    directive.onBlur(event);
    expect(event.target.value).toEqual('xyz');
  });
});
