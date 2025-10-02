import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CartService } from '../../../core/services/cart.service';
import { CartItem } from '../../../shared/models/cart.model';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  items: CartItem[] = [];

  constructor(
    private readonly cartService: CartService,
    private readonly router: Router,
    private readonly dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.items = this.cartService.getItems();
    this.cartService.cartChanges().subscribe(items => (this.items = items));
  }

  get totals() {
    return this.cartService.totals();
  }

  updateQuantity(item: CartItem, quantity: number): void {
    this.cartService.updateQuantity(item.product.id, quantity);
  }

  remove(item: CartItem): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '360px',
      data: {
        title: 'Remove item',
        message: `Remove ${item.product.name} from your cart?`,
        confirmLabel: 'Remove',
        cancelLabel: 'Keep'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cartService.remove(item.product.id);
      }
    });
  }

  checkout(): void {
    this.router.navigate(['/checkout']);
  }
}
