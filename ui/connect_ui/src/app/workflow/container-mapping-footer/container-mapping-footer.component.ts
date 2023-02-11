import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-container-mapping-footer',
  templateUrl: './container-mapping-footer.component.html',
  styleUrls: ['./container-mapping-footer.component.scss']
})
export class ContainerMappingFooterComponent implements OnInit {

  @Input() sampleCount: number;
  @Output() onReTrying: EventEmitter<boolean> = new EventEmitter();
  @Output() onConfirming: EventEmitter<boolean> = new EventEmitter();
  
  constructor() { }

  ngOnInit() {
  }

  public onRetry(){
    this.onReTrying.emit(true);
  }

  public onConfirmingUpload(){
    this.onConfirming.emit(true);
  }
}
