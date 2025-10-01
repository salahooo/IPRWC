import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loading = false;

  readonly form = this.fb.group({
    usernameOrEmail: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar
  ) {}

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    this.authService.login(this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.snackBar.open('Welcome back!', undefined, { duration: 3000 });
        this.router.navigate(['/']);
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Invalid credentials', 'Close', { duration: 4000 });
      }
    });
  }
}
