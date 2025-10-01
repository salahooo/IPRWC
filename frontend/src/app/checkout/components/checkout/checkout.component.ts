import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from '../../../core/services/cart.service';
import { OrderService } from '../../../core/services/order.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent {
  placingOrder = false;
  successOrderId?: number;

  constructor(
    readonly cartService: CartService,
    private readonly orderService: OrderService,
    private readonly snackBar: MatSnackBar,
    private readonly router: Router
  ) {}

  placeOrder(): void {
    const items = this.cartService.getItems();
    if (!items.length) {
      return;
    }
    this.placingOrder = true;
    const payload = {
      items: items.map(item => ({
        productId: item.product.id,
        quantity: item.quantity,
        priceAtPurchase: item.product.price
      }))
    };
    this.orderService.create(payload).subscribe({
      next: order => {
        this.placingOrder = false;
        this.successOrderId = order.id;
        this.cartService.clear();
        this.snackBar.open(`Order #${order.id} placed successfully`, undefined, { duration: 4000 });
      },
      error: error => {
        this.placingOrder = false;
        this.snackBar.open(error.error?.message || 'Checkout failed', 'Close', { duration: 5000 });
      }
    });
  }

  viewOrders(): void {
    this.router.navigate(['/profile/orders']);
  }
}
