import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from '../../../core/services/cart.service';
import { CartItem } from '../../../shared/models/cart.model';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  items: CartItem[] = [];

  constructor(private readonly cartService: CartService, private readonly router: Router) {}

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
    this.cartService.remove(item.product.id);
  }

  checkout(): void {
    this.router.navigate(['/checkout']);
  }
}
