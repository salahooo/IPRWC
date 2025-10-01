import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CartItem, CartTotals } from '../../shared/models/cart.model';

const CART_KEY = 'fatbike_cart';
const TAX_RATE = 0.21;

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly cart$ = new BehaviorSubject<CartItem[]>(this.load());

  cartChanges() {
    return this.cart$.asObservable();
  }

  getItems(): CartItem[] {
    return this.cart$.value;
  }

  addItem(item: CartItem): void {
    const existing = this.cart$.value.find(i => i.product.id === item.product.id);
    if (existing) {
      existing.quantity += item.quantity;
    } else {
      this.cart$.next([...this.cart$.value, item]);
    }
    this.persist();
  }

  updateQuantity(productId: number, quantity: number): void {
    const updated = this.cart$.value.map(item =>
      item.product.id === productId ? { ...item, quantity } : item
    );
    this.cart$.next(updated.filter(item => item.quantity > 0));
    this.persist();
  }

  remove(productId: number): void {
    this.cart$.next(this.cart$.value.filter(item => item.product.id !== productId));
    this.persist();
  }

  clear(): void {
    this.cart$.next([]);
    localStorage.removeItem(CART_KEY);
  }

  totals(): CartTotals {
    const subtotal = this.cart$.value.reduce((sum, item) =>
      sum + item.product.price * item.quantity, 0);
    const tax = subtotal * TAX_RATE;
    return {
      subtotal,
      tax,
      total: subtotal + tax
    };
  }

  private persist(): void {
    localStorage.setItem(CART_KEY, JSON.stringify(this.cart$.value));
  }

  private load(): CartItem[] {
    const raw = localStorage.getItem(CART_KEY);
    if (!raw) {
      return [];
    }
    try {
      return JSON.parse(raw) as CartItem[];
    } catch (error) {
      return [];
    }
  }
}
