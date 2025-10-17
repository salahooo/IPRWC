import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { combineLatest, map, Observable, startWith } from 'rxjs';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { Product, ProductCategory, resolveCategory } from '../../../shared/models/product.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-catalog-list',
  templateUrl: './catalog-list.component.html',
  styleUrls: ['./catalog-list.component.scss']
})
export class CatalogListComponent implements OnInit {
  readonly searchControl = new FormControl('', { nonNullable: true });
  readonly sortControl = new FormControl('default', { nonNullable: true });
  readonly categoryControl = new FormControl<'All' | ProductCategory>('All', { nonNullable: true });

  products$!: Observable<Product[]>;
  filtered$!: Observable<Product[]>;

  categories: Array<'All' | ProductCategory> = [
    'All',
    'CPUs',
    'GPUs',
    'Motherboards',
    'Memory',
    'Storage',
    'Cases',
    'Power Supplies',
    'Cooling',
    'Monitors',
    'Peripherals'
  ];

  constructor(
    private readonly productService: ProductService,
    private readonly cartService: CartService,
    private readonly snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.products$ = this.productService.list();
    // Reactively filter and sort the catalog whenever any control or the source list changes
    this.filtered$ = combineLatest([
      this.products$,
      this.searchControl.valueChanges.pipe(startWith('')),
      this.sortControl.valueChanges.pipe(startWith('default')),
      this.categoryControl.valueChanges.pipe(startWith<'All' | ProductCategory>('All'))
    ]).pipe(
      map(([products, search, sort, category]) => {
        const normalized = search?.toLowerCase() ?? '';
        // Filter by SKU/name match first, then apply category and ordering constraints
        let result = products.filter(product =>
          product.name.toLowerCase().includes(normalized) ||
          product.sku.toLowerCase().includes(normalized)
        );
        if (category !== 'All') {
          result = result.filter(product => resolveCategory(product.sku) === category);
        }
        if (sort === 'priceAsc') {
          result = [...result].sort((a, b) => a.price - b.price);
        } else if (sort === 'priceDesc') {
          result = [...result].sort((a, b) => b.price - a.price);
        }
        return result;
      })
    );
  }

  addToCart(product: Product): void {
    // Add a single unit and provide immediate feedback via snackbar
    this.cartService.addItem({ product, quantity: 1 });
    this.snackBar.open(`${product.name} added to cart`, undefined, { duration: 3000 });
  }

  trackById(_: number, product: Product): number {
    // Keep Angular from re-rendering unchanged cards when the order shifts
    return product.id;
  }
}
