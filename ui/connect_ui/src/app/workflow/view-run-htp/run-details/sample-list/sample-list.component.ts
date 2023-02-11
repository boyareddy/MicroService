import { Component, OnInit, Input, OnChanges, ViewChild, Output, EventEmitter } from '@angular/core';
import { MatSort, MatTableDataSource } from '@angular/material';


@Component({
  selector: 'app-sample-list',
  templateUrl: './sample-list.component.html',
  styleUrls: ['./sample-list.component.scss']
})

export class SampleListComponent implements OnInit, OnChanges {

  @Input() samplelist: any;
  @Input() displayedColumns: any;
  @Input() columnsToDisplay: any;
  @Input() processStepName: string;
  @ViewChild(MatSort) sort: MatSort;
  @Output() commentsResponse: EventEmitter<any> = new EventEmitter();
  public dataSource: MatTableDataSource<any>;
  processStep;

  ngOnInit() {
    console.log(this.processStepName, 'oninit stage');
  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngOnChanges() {
    this.dataSource = new MatTableDataSource(this.samplelist);
    this.dataSource.sort = this.sort;
    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, header) => data[header];
  }

  response(event: any) {
    console.log(event);
    this.commentsResponse.emit(event);
  }

  isEmptyFlag (value) {
    if (value === '' || !value) {
      return true;
    } else {
      return false;
    }
  }

  update(el: any, comment: string) {
    if (comment == null) { return; }
    // copy and mutate
    console.log(el, comment);
    const event = {
      accesssioningId: el.accessioningId,
      comments: comment,
      processStepName: el.processStepName
    };
    console.log(event);
    this.commentsResponse.emit(event);
  }
}

