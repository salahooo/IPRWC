import { Product } from './product.model';

export type OrderStatus = 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  productSku: string;
  quantity: number;
  priceAtPurchase: number;
  imageUrl: string;
}

export interface Order {
  id: number;
  totalPrice: number;
  status: OrderStatus;
  createdAt: string;
  items: OrderItem[];
  userEmail: string;
  userFullName: string;
}

export interface OrderCreateItem {
  productId: number;
  quantity: number;
  priceAtPurchase: number;
}

export interface OrderCreateRequest {
  items: OrderCreateItem[];
}

export const ORDER_STATUS_OPTIONS: OrderStatus[] = ['PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED'];
