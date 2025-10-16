import { Component, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, ValidationErrors, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../../core/services/user.service';
import { OrderService } from '../../../core/services/order.service';
import { UserProfile } from '../../../shared/models/user.model';
import { Order } from '../../../shared/models/order.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  profile?: UserProfile;
  orders: Order[] = [];
  loadingOrders = false;

  private readonly fb = inject(FormBuilder);
  private readonly userService = inject(UserService);
  private readonly orderService = inject(OrderService);
  private readonly snackBar = inject(MatSnackBar);

  readonly profileForm = this.fb.group({
    fullName: this.fb.nonNullable.control('', Validators.required),
    gender: this.fb.control<string | null>(''),
    email: this.fb.nonNullable.control('', [Validators.required, Validators.email]),
    dateOfBirth: this.fb.control<Date | null>(null, Validators.required)
  });

  readonly passwordForm = this.fb.group(
    {
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    },
    { validators: (control: AbstractControl): ValidationErrors | null => this.passwordsMatchValidator(control) }
  );

  hideOld = true;
  hideNew = true;
  hideConfirm = true;


  ngOnInit(): void {
    this.loadProfile();
    this.loadOrders();
  }

  private loadProfile(): void {
    this.userService.me().subscribe(profile => {
      this.profile = profile;
      this.profileForm.patchValue({
        fullName: profile.fullName,
        gender: profile.gender ?? '',
        email: profile.email,
        dateOfBirth: profile.dateOfBirth ? new Date(profile.dateOfBirth) : null
      });
    });
  }

  private loadOrders(): void {
    this.loadingOrders = true;
    this.orderService.myOrders().subscribe({
      next: orders => {
        this.orders = orders;
        this.loadingOrders = false;
      },
      error: () => (this.loadingOrders = false)
    });
  }

  saveProfile(): void {
    if (this.profileForm.invalid) {
      return;
    }
    const value = this.profileForm.getRawValue();
    if (!value.dateOfBirth) {
      return;
    }
    this.userService.update({
      fullName: value.fullName,
      gender: value.gender ?? undefined,
      email: value.email,
      dateOfBirth: value.dateOfBirth.toISOString().split('T')[0]
    }).subscribe({
      next: profile => {
        this.profile = profile;
        this.snackBar.open('Profile updated', undefined, { duration: 3000 });
      },
      error: error => this.snackBar.open(error.error?.message || 'Update failed', 'Close', { duration: 4000 })
    });
  }

  changePassword(): void {
    if (this.passwordForm.invalid) {
      return;
    }
    const { oldPassword, newPassword } = this.passwordForm.value as any;
    this.userService.changePassword({ oldPassword, newPassword }).subscribe({
      next: () => {
        this.snackBar.open('Password updated', undefined, { duration: 3000 });
        this.passwordForm.reset();
      },
      error: error => this.snackBar.open(error.error?.message || 'Password update failed', 'Close', { duration: 4000 })
    });
  }

  private passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const newPassword = control.get('newPassword')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    if (newPassword && confirmPassword && newPassword !== confirmPassword) {
      return { passwordMismatch: true };
    }
    return null;
  }
}





