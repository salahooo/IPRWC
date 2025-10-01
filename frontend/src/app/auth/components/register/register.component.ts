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
    username: this.fb.nonNullable.control('', [Validators.required, Validators.minLength(3)]),
    email: this.fb.nonNullable.control('', [Validators.required, Validators.email]),
    password: this.fb.nonNullable.control('', [Validators.required, Validators.minLength(8)]),
    fullName: this.fb.nonNullable.control('', [Validators.required]),
    gender: this.fb.control<string | null>(null),
    dateOfBirth: this.fb.control<Date | null>(null, [Validators.required])
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
    const { dateOfBirth, gender, ...rest } = this.form.getRawValue();
    if (!dateOfBirth) {
      return;
    }
    const payload = {
      ...rest,
      gender: gender || undefined,
      dateOfBirth: dateOfBirth.toISOString().split('T')[0]
    };
    this.authService.register(payload).subscribe({
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
