import { Component, EventEmitter, Output } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';

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
  readonly cartCount$: Observable<number>;

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly cartService: CartService
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn();
    this.roles$ = this.authService.rolesChanges();
    this.username$ = this.authService.usernameChanges();
    this.cartCount$ = this.cartService.cartChanges().pipe(
      map(items => items.reduce((sum, it) => sum + it.quantity, 0))
    );
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/');
  }
}
