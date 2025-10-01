import { Component, EventEmitter, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  @Output() toggleSidenav = new EventEmitter<void>();
  readonly isLoggedIn$: Observable<boolean> = this.authService.isLoggedIn();
  readonly roles$ = this.authService.rolesChanges();
  readonly username$ = this.authService.usernameChanges();

  constructor(private readonly authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}
