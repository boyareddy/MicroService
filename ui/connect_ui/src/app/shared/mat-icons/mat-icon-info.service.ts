import { Injectable } from '@angular/core';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class MatIconInfoService {

  constructor(private _iconRegistry: MatIconRegistry,
    private _sanitizer: DomSanitizer) {
     }

     initIcons() {
      this._iconRegistry
      .addSvgIcon('rc-warning', this._sanitizer.bypassSecurityTrustResourceUrl('assets/Images/status/flags/warning.svg'))
      .addSvgIcon('rc-error', this._sanitizer.bypassSecurityTrustResourceUrl('assets/Images/status/flags/error.svg'))
      .addSvgIcon('rc-error-30', this._sanitizer.bypassSecurityTrustResourceUrl('assets/Images/status/flags/30x30/error.svg'))
      .addSvgIcon('rc-warning-30', this._sanitizer.bypassSecurityTrustResourceUrl('assets/Images/status/flags/30x30/warning.svg'))
      .addSvgIcon('rc-edit-icon', this._sanitizer.bypassSecurityTrustResourceUrl('assets/Images/Icons/iconsEdit.svg'));
     }
}
