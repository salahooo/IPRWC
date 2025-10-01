import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { CatalogRoutingModule } from './catalog-routing.module';
import { CatalogListComponent } from './components/catalog-list/catalog-list.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';

@NgModule({
  declarations: [CatalogListComponent, ProductDetailComponent],
  imports: [SharedModule, CatalogRoutingModule]
})
export class CatalogModule {}
