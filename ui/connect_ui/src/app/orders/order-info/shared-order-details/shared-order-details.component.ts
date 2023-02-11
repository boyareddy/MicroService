import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-shared-order-details',
  templateUrl: './shared-order-details.component.html',
  styleUrls: ['./shared-order-details.component.scss']
})
export class SharedOrderDetailsComponent implements OnInit {

  @Input('field1') field1: any;
  @Input('field2') field2: any;
  @Input('singleRow') singleRow: boolean;
  constructor() { }

  ngOnInit() {
  }

}

class Fields {
  field1: {key: '', value: '' };
  field2?: {key: '', value: '' };
}
