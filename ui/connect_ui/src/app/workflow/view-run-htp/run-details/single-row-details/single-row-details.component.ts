import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-single-row-details',
  templateUrl: './single-row-details.component.html',
  styleUrls: ['./single-row-details.component.scss']
})
export class SingleRowDetailsComponent implements OnInit {

  @Input() i18key: string;
  @Input() value: string;
  @Input() singleRow: boolean;
  @Input() isDate = false;
  @Input() inputFor: string;

  inputReagentCardKey = 'reagent';
  inputSampleCardKey = 'sample';

  constructor() { }

  ngOnInit() {
  }

}
