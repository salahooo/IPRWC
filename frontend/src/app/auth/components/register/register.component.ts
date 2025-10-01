import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  loading = false;

  readonly form = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    fullName: ['', [Validators.required]],
    gender: [''],
    dateOfBirth: ['', [Validators.required]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly snackBar: MatSnackBar,
    private readonly router: Router
  ) {}

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.loading = true;
    const payload = { ...this.form.value, dateOfBirth: this.form.value.dateOfBirth?.toISOString().split('T')[0] };
    this.authService.register(payload as any).subscribe({
      next: () => {
        this.loading = false;
        this.snackBar.open('Account created! Please login.', undefined, { duration: 4000 });
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open(error.error?.message || 'Registration failed', 'Close', { duration: 5000 });
      }
    });
  }
}
