import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  sidenavOpened = false;
  readonly currentYear = new Date().getFullYear();
  readonly isLoggedIn$: Observable<boolean>;
  readonly roles$: Observable<string[]>;

  constructor(private readonly authService: AuthService) {
    this.isLoggedIn$ = this.authService.isLoggedIn();
    this.roles$ = this.authService.rolesChanges();
  }

  toggleSidenav(): void {
    this.sidenavOpened = !this.sidenavOpened;
  }

  closeSidenav(): void {
    this.sidenavOpened = false;
  }
}

