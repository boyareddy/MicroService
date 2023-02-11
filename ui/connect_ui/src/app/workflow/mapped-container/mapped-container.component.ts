import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-mapped-container',
  templateUrl: './mapped-container.component.html',
  styleUrls: ['./mapped-container.component.scss']
})
export class MappedContainerComponent implements OnInit {

  @Input() containerId: string;
  @Input() containerType: string;
  @Input() samples: any = [];
  @Input() labelsInfo: any;
 
  displayedColumns: string[] = ['accessioningID', 'position'];

  constructor() { }

  ngOnInit() {
    console.log('Test>', this.containerId, this.containerType, this.samples);
  }

}
