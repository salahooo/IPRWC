import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/auth.model';
import { UserProfile } from '../../shared/models/user.model';
import { environment } from '../../../environments/environment';
import { CartService } from './cart.service';

const TOKEN_KEY = 'pcparts_token';
const ROLES_KEY = 'pcparts_roles';
const USERNAME_KEY = 'pcparts_username';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}`;
  private readonly loggedIn$ = new BehaviorSubject<boolean>(this.hasToken());
  private readonly roles$ = new BehaviorSubject<string[]>(this.getStoredRoles());
  private readonly username$ = new BehaviorSubject<string | null>(localStorage.getItem(USERNAME_KEY));

  constructor(private readonly http: HttpClient, private readonly cartService: CartService) {
    // Ensure the cart points at the restored user when the service boots
    this.cartService.setActiveUser(this.username$.value);
  }

  login(payload: LoginRequest): Observable<AuthResponse> {
    // Persist the issued token/roles once the API confirms the login
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, payload).pipe(
      tap(response => this.persistAuth(response))
    );
  }

  register(payload: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/auth/register`, payload);
  }

  logout(): void {
    // Drop local session data and reset any user-specific state
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLES_KEY);
    localStorage.removeItem(USERNAME_KEY);
    this.cartService.clear();
    this.cartService.setActiveUser(null);
    this.loggedIn$.next(false);
    this.roles$.next([]);
    this.username$.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn$.asObservable();
  }

  hasRole(role: string): boolean {
    return this.roles$.value.includes(role);
  }

  rolesChanges(): Observable<string[]> {
    return this.roles$.asObservable();
  }

  usernameChanges(): Observable<string | null> {
    return this.username$.asObservable();
  }

  private persistAuth(response: AuthResponse): void {
    // Store auth artefacts and push new values to consumers in one place
    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(ROLES_KEY, JSON.stringify(response.roles));
    localStorage.setItem(USERNAME_KEY, response.username);
    this.cartService.setActiveUser(response.username);
    this.loggedIn$.next(true);
    this.roles$.next(response.roles);
    this.username$.next(response.username);
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(TOKEN_KEY);
  }

  private getStoredRoles(): string[] {
    const raw = localStorage.getItem(ROLES_KEY);
    return raw ? JSON.parse(raw) : [];
  }
}
