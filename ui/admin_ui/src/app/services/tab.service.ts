import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TabService {
  selectedTab: any;
  constructor() { }

  /**
   * setTab() i to set the selected tab information
  */
  public setTab(selectedTab: any): void {
    this.selectedTab = selectedTab;
  }

  /**
   * getTab() is to get the selcted tab information
  */
  public getTab(): any {
    return this.selectedTab;
  }

  /**
   * deleteTab() is to remove the tab information
  */
  public deleteTab(): any {
    this.selectedTab = undefined;
  }

  /**
   * getSelectedTabIndex() returns the index of the selected tab
  */
  public getSelectedTabIndex(): number {
    if (this.selectedTab) {
      return this.selectedTab.index;
    } else {
      return 0;
    }
  }
}
