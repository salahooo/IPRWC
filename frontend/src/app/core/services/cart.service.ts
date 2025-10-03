import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CartItem, CartTotals } from '../../shared/models/cart.model';

const CART_KEY = 'fatbike_cart';
const TAX_RATE = 0.21;

@Injectable({ providedIn: 'root' })
export class CartService {
  private storageKey = CART_KEY;
  private readonly cart$ = new BehaviorSubject<CartItem[]>(this.load());

  cartChanges() {
    return this.cart$.asObservable();
  }

  getItems(): CartItem[] {
    return this.cart$.value;
  }

  setActiveUser(username: string | null): void {
    this.storageKey = username ? `${CART_KEY}_${username}` : CART_KEY;
    this.cart$.next(this.load());
  }

  addItem(item: CartItem): void {
    const updated = this.cart$.value.some(existing => existing.product.id === item.product.id)
      ? this.cart$.value.map(existing =>
          existing.product.id === item.product.id
            ? { ...existing, quantity: existing.quantity + item.quantity }
            : existing
        )
      : [...this.cart$.value, item];
    this.cart$.next(updated);
    this.persist(updated);
  }

  updateQuantity(productId: number, quantity: number): void {
    const updated = this.cart$.value
      .map(item => (item.product.id === productId ? { ...item, quantity } : item))
      .filter(item => item.quantity > 0);
    this.cart$.next(updated);
    this.persist(updated);
  }

  remove(productId: number): void {
    const filtered = this.cart$.value.filter(item => item.product.id !== productId);
    this.cart$.next(filtered);
    this.persist(filtered);
  }

  clear(): void {
    this.cart$.next([]);
    localStorage.removeItem(this.storageKey);
  }

  totals(): CartTotals {
    const subtotal = this.cart$.value.reduce(
      (sum, item) => sum + item.product.price * item.quantity,
      0
    );
    const tax = subtotal * TAX_RATE;
    return {
      subtotal,
      tax,
      total: subtotal + tax
    };
  }

  private persist(items: CartItem[] = this.cart$.value): void {
    localStorage.setItem(this.storageKey, JSON.stringify(items));
  }

  private load(): CartItem[] {
    const raw = localStorage.getItem(this.storageKey);
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
