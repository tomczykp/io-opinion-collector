import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ProductsService} from "../../../services/products.service";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html'
})
export class AddProductComponent implements OnInit {
  regex: RegExp = new RegExp('^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$');

  addProductForm = new FormGroup({
    categoryId: new FormControl('', [Validators.required, Validators.pattern(this.regex)]),
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    properties: new FormControl('')
  })

  get categoryId() {
    return this.addProductForm.get('categoryId');
  }

  get name() {
    return this.addProductForm.get('name');
  }

  get description() {
    return this.addProductForm.get('description');
  }

  get properties() {
    return this.addProductForm.get('properties');
  }

  constructor(
    private productService: ProductsService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
  }


  addProduct() {
    if (this.addProductForm.valid) {
      const ProductDTO: object = {
        "categoryId": this.addProductForm.getRawValue().categoryId,
        "name": this.addProductForm.getRawValue().name,
        "description": this.addProductForm.getRawValue().description,
        "properties": {
          "test": "test"
        }
      }
      console.log(ProductDTO);
      this.productService.addProduct(ProductDTO)
        .subscribe((result) => {
            if (result.status === 200) {
              this.router.navigate(['/']);
            }
          }
        )
    }
  }
}
