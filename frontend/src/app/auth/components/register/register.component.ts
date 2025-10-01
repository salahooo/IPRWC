import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RegisterRequest } from '../../../shared/models/auth.model';
import { AuthService } from '../../../core/services/auth.service';

type RegisterFormValue = {
  username: string;
  email: string;
  password: string;
  fullName: string;
  gender: string;
  dateOfBirth: Date | string | null;
};

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
    dateOfBirth: [null, [Validators.required]]
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
    const formValue = this.form.getRawValue() as RegisterFormValue;
    const payload: RegisterRequest = {
      username: formValue.username,
      email: formValue.email,
      password: formValue.password,
      fullName: formValue.fullName,
      gender: formValue.gender || undefined,
      dateOfBirth: this.toDateString(formValue.dateOfBirth)
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

  private toDateString(date: Date | string | null): string {
    if (date instanceof Date) {
      return date.toISOString().split('T')[0];
    }

    return date ?? '';
  }
}
