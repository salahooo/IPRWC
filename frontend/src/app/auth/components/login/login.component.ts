import { Component, inject } from '@angular/core';
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

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  readonly form = this.fb.group({
    usernameOrEmail: this.fb.nonNullable.control('', Validators.required),
    password: this.fb.nonNullable.control('', Validators.required)
  });


  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    const { usernameOrEmail, password } = this.form.getRawValue();
    this.authService.login({ usernameOrEmail, password }).subscribe({
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




