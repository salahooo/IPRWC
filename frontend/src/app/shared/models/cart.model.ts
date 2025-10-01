import { Product } from './product.model';

export interface CartItem {
  product: Product;
  quantity: number;
}

export interface CartTotals {
  subtotal: number;
  tax: number;
  total: number;
}
