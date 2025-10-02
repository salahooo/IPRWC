import { Component, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Product } from '../../shared/models/product.model';

@Component({
  selector: 'app-product-dialog',
  templateUrl: './product-dialog.component.html',
  styleUrls: ['./product-dialog.component.scss']
})
export class ProductDialogComponent {
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<ProductDialogComponent>);
  private readonly product = inject<Product | null>(MAT_DIALOG_DATA);

  readonly form = this.fb.group({
    name: ['', Validators.required],
    sku: ['', Validators.required],
    price: [0, [Validators.required, Validators.min(0)]],
    description: ['', [Validators.required, Validators.maxLength(1000)]],
    imageUrl: ['', Validators.required],
    stock: [0, [Validators.required, Validators.min(0)]]
  });

  constructor() {
    if (this.product) {
      this.form.patchValue(this.product);
    }
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.dialogRef.close(this.form.value);
  }
}


