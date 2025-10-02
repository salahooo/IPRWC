import { Component, EventEmitter, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  @Output() toggleSidenav = new EventEmitter<void>();

  readonly isLoggedIn$: Observable<boolean>;
  readonly roles$: Observable<string[]>;
  readonly username$: Observable<string | null>;

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn();
    this.roles$ = this.authService.rolesChanges();
    this.username$ = this.authService.usernameChanges();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/');
  }
}
