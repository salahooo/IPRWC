export interface Product {
  id: number;
  name: string;
  sku: string;
  price: number;
  description: string;
  imageUrl: string;
  stock: number;
  createdAt: string;
}

export type ProductCategory = 'Parts' | 'Bikes';

export const resolveCategory = (sku: string): ProductCategory =>
  sku.startsWith('FB-BIKE') ? 'Bikes' : 'Parts';
