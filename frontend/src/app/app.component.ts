import { Component } from '@angular/core';
import { Observable, map } from 'rxjs';
import { AuthService } from './core/services/auth.service';
import { CartService } from './core/services/cart.service';

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
  readonly cartCount$: Observable<number>;

  constructor(
    private readonly authService: AuthService,
    private readonly cartService: CartService
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn();
    this.roles$ = this.authService.rolesChanges();
    this.cartCount$ = this.cartService.cartChanges().pipe(
      map(items => items.reduce((sum, it) => sum + it.quantity, 0))
    );
  }

  toggleSidenav(): void {
    this.sidenavOpened = !this.sidenavOpened;
  }

  closeSidenav(): void {
    this.sidenavOpened = false;
  }
}

