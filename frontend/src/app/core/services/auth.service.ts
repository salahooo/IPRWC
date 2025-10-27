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

  constructor(
    private readonly http: HttpClient,
    private readonly cartService: CartService
  ) {
    // Ensure the cart points at the restored user when the service boots
    this.cartService.setActiveUser(this.username$.value);
  }

  /**
   * Logs in a user. The backend expects "usernameOrEmail" instead of "username".
   */
  login(payload: LoginRequest): Observable<AuthResponse> {
    // Normalize payload key for backend compatibility
    const body = {
      usernameOrEmail: (payload as any).username ?? (payload as any).usernameOrEmail,
      password: payload.password
    };

    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, body).pipe(
      tap(response => this.persistAuth(response))
    );
  }

  /**
   * Registers a new user
   */
  register(payload: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/auth/register`, payload);
  }

  /**
   * Logs out the current user and clears session data
   */
  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLES_KEY);
    localStorage.removeItem(USERNAME_KEY);
    this.cartService.clear();
    this.cartService.setActiveUser(null);
    this.loggedIn$.next(false);
    this.roles$.next([]);
    this.username$.next(null);
  }

  /**
   * Retrieves JWT token
   */
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  /**
   * Observable for login state
   */
  isLoggedIn(): Observable<boolean> {
    return this.loggedIn$.asObservable();
  }

  /**
   * Role utilities
   */
  hasRole(role: string): boolean {
    return this.roles$.value.includes(role);
  }

  rolesChanges(): Observable<string[]> {
    return this.roles$.asObservable();
  }

  usernameChanges(): Observable<string | null> {
    return this.username$.asObservable();
  }

  /**
   * Persists authentication data
   */
  private persistAuth(response: AuthResponse): void {
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
