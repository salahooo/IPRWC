import { Component, OnInit } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ProductService } from '../../services/product.service';
import { Product, resolveCategory } from '../../../shared/models/product.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  products$!: Observable<Product[]>;
  featured$!: Observable<Product[]>;

  constructor(private readonly productService: ProductService) {}

  ngOnInit(): void {
    this.products$ = this.productService.list();
    this.featured$ = this.products$.pipe(
      map(products => products.slice(0, 4))
    );
  }

  categoryFor(product: Product): string {
    return resolveCategory(product.sku);
  }
}
