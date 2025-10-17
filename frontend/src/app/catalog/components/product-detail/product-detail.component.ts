import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, EMPTY, map, Observable, switchMap, throwError } from 'rxjs';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { Product, resolveCategory } from '../../../shared/models/product.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  product$!: Observable<Product>;
  quantity = 1;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly productService: ProductService,
    private readonly cartService: CartService,
    private readonly snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.product$ = this.route.paramMap.pipe(
      map(params => Number(params.get('id'))),
      switchMap(id => {
        if (!Number.isInteger(id) || id <= 0) {
          this.router.navigate(['/not-found']);
          return EMPTY;
        }
        return this.productService.findById(id).pipe(
          catchError(error => {
            if (error.status === 404) {
              this.router.navigate(['/not-found']);
              return EMPTY;
            }
            return throwError(() => error);
          })
        );
      })
    );
  }

  addToCart(product: Product): void {
    this.cartService.addItem({ product, quantity: this.quantity });
    this.snackBar.open(`${product.name} added to cart`, undefined, { duration: 3000 });
  }

  category(product: Product): string {
    return resolveCategory(product.sku);
  }
}
