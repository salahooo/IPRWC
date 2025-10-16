import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './components/admin.component';
import { ProductDialogComponent } from './dialogs/product-dialog.component';
import { ResetPasswordDialogComponent } from './dialogs/reset-password-dialog.component';

@NgModule({
  declarations: [AdminComponent, ProductDialogComponent, ResetPasswordDialogComponent],
  imports: [SharedModule, AdminRoutingModule]
})
export class AdminModule {}
