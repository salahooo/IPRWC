import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { CheckoutRoutingModule } from './checkout-routing.module';
import { CheckoutComponent } from './components/checkout/checkout.component';

@NgModule({
  declarations: [CheckoutComponent],
  imports: [SharedModule, CheckoutRoutingModule]
})
export class CheckoutModule {}
