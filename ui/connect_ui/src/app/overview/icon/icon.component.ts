import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-icon',
  templateUrl: './icon.component.html',
  styleUrls: ['./icon.component.scss']
})
export class IconComponent implements OnInit {
  @Input() IconDetails;
  @Output() navigate = new EventEmitter();
  constructor() {}

  ngOnInit() {}

  navigateTo(iconDetails) {
    this.navigate.emit(iconDetails);
  }

}
