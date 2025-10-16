import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductService } from '../../core/services/product.service';
import { OrderService } from '../../core/services/order.service';
import { UserService } from '../../core/services/user.service';
import { Product } from '../../shared/models/product.model';
import { Order, ORDER_STATUS_OPTIONS } from '../../shared/models/order.model';
import { UserProfile } from '../../shared/models/user.model';
import { ProductDialogComponent } from '../dialogs/product-dialog.component';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';
import { ResetPasswordDialogComponent } from '../dialogs/reset-password-dialog.component';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  products: Product[] = [];
  orders: Order[] = [];
  users: UserProfile[] = [];

  productSearch = new FormControl('', { nonNullable: true });

  readonly statusOptions = ORDER_STATUS_OPTIONS;

  constructor(
    private readonly productService: ProductService,
    private readonly orderService: OrderService,
    private readonly userService: UserService,
    private readonly dialog: MatDialog,
    private readonly snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadOrders();
    this.loadUsers();
  }

  get filteredProducts(): Product[] {
    const term = this.productSearch.value.toLowerCase();
    return this.products.filter(product =>
      product.name.toLowerCase().includes(term) || product.sku.toLowerCase().includes(term)
    );
  }

  loadProducts(): void {
    this.productService.list().subscribe(products => (this.products = products));
  }

  loadOrders(): void {
    this.orderService.findAll().subscribe(orders => (this.orders = orders));
  }

  loadUsers(): void {
    this.userService.findAll().subscribe(users => (this.users = users));
  }

  openProductDialog(product?: Product): void {
    const dialogRef = this.dialog.open(ProductDialogComponent, {
      width: '520px',
      data: product ? { ...product } : null
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const payload = {
          name: result.name,
          sku: result.sku,
          price: Number(result.price),
          description: result.description,
          imageUrl: result.imageUrl,
          stock: Number(result.stock)
        };
        if (product) {
          this.productService.update(product.id, payload).subscribe({
            next: updated => {
              this.snackBar.open('Product updated', undefined, { duration: 3000 });
              this.loadProducts();
            },
            error: error => this.snackBar.open(error.error?.message || 'Update failed', 'Close', { duration: 4000 })
          });
        } else {
          this.productService.create(payload).subscribe({
            next: created => {
              this.snackBar.open('Product created', undefined, { duration: 3000 });
              this.products = [...this.products, created];
            },
            error: error => this.snackBar.open(error.error?.message || 'Creation failed', 'Close', { duration: 4000 })
          });
        }
      }
    });
  }

  deleteProduct(product: Product): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '360px',
      data: {
        title: 'Delete product',
        message: `Are you sure you want to delete ${product.name}?`,
        confirmLabel: 'Delete',
        cancelLabel: 'Cancel'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }
      this.productService.delete(product.id).subscribe(() => {
        this.products = this.products.filter(p => p.id !== product.id);
        this.snackBar.open('Product deleted', undefined, { duration: 3000 });
      });
    });
  }

  updateStatus(order: Order, status: string): void {
    this.orderService.updateStatus(order.id, status).subscribe(updated => {
      order.status = updated.status;
      this.snackBar.open('Order status updated', undefined, { duration: 3000 });
    });
  }

  deleteUser(user: UserProfile): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '360px',
      data: {
        title: 'Remove user',
        message: `Remove user ${user.username}?`,
        confirmLabel: 'Remove',
        cancelLabel: 'Cancel'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }
      this.userService.deleteUser(user.id).subscribe(() => {
        this.users = this.users.filter(u => u.id !== user.id);
        this.snackBar.open('User deleted', undefined, { duration: 3000 });
      });
    });
  }

  openResetPasswordDialog(user: UserProfile): void {
    const dialogRef = this.dialog.open(ResetPasswordDialogComponent, {
      width: '420px',
      data: { username: user.username }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }
      const newPassword: string = result.newPassword;
      this.userService.adminResetPassword(user.id, newPassword).subscribe({
        next: () => this.snackBar.open('Password reset', undefined, { duration: 3000 }),
        error: error => this.snackBar.open(error.error?.message || 'Password reset failed', 'Close', { duration: 4000 })
      });
    });
  }
}
