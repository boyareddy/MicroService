import { CanActivate } from '@angular/router';
export class AuthGaurd implements CanActivate {
    canActivate() {
        return true;
    }
}
