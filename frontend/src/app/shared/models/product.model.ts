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

export type ProductCategory =
  | 'CPUs'
  | 'GPUs'
  | 'Motherboards'
  | 'Memory'
  | 'Storage'
  | 'Cases'
  | 'Power Supplies'
  | 'Cooling'
  | 'Monitors'
  | 'Peripherals';

const CATEGORY_PREFIXES: Array<[string, ProductCategory]> = [
  ['CPU-', 'CPUs'],
  ['GPU-', 'GPUs'],
  ['MB-', 'Motherboards'],
  ['RAM-', 'Memory'],
  ['SSD-', 'Storage'],
  ['CASE-', 'Cases'],
  ['PSU-', 'Power Supplies'],
  ['COOLER-', 'Cooling'],
  ['MONITOR-', 'Monitors'],
  ['PERIPHERAL-', 'Peripherals']
];

export const resolveCategory = (sku: string): ProductCategory => {
  const normalized = sku.toUpperCase();
  for (const [prefix, category] of CATEGORY_PREFIXES) {
    if (normalized.startsWith(prefix)) {
      return category;
    }
  }
  return 'Peripherals';
};
