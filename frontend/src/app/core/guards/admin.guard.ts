import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable, map } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private readonly authService: AuthService, private readonly router: Router) {}

  canActivate(): Observable<boolean | UrlTree> {
    return this.authService.rolesChanges().pipe(
      map(roles => (roles.includes('ROLE_ADMIN') ? true : this.router.createUrlTree(['/forbidden'])))
    );
  }
}
