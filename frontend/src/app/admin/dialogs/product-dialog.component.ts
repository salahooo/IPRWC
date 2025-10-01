import { Component, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Product } from '../../shared/models/product.model';

@Component({
  selector: 'app-product-dialog',
  templateUrl: './product-dialog.component.html',
  styleUrls: ['./product-dialog.component.scss']
})
export class ProductDialogComponent {
  readonly form = this.fb.group({
    name: ['', Validators.required],
    sku: ['', Validators.required],
    price: [0, [Validators.required, Validators.min(0)]],
    description: ['', [Validators.required, Validators.maxLength(1000)]],
    imageUrl: ['', Validators.required],
    stock: [0, [Validators.required, Validators.min(0)]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly dialogRef: MatDialogRef<ProductDialogComponent>,
    @Inject(MAT_DIALOG_DATA) product: Product | null
  ) {
    if (product) {
      this.form.patchValue(product);
    }
  }

  submit(): void {
    if (this.form.invalid) {
      return;
    }
    this.dialogRef.close(this.form.value);
  }
}
