import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Product} from "../../../model/Product";
import {ProductsService} from "../../../services/products.service";
import {HttpResponse} from "@angular/common/http";


@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html'
})
export class UpdateProductComponent implements OnInit {
  product: Product = <Product>{};
  match: RegExpMatchArray | null;
  uuid: string;

  regexForm: RegExp = new RegExp('^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$');
  regexGet: RegExp = new RegExp('[0-9a-f\-]+$');


  updateProductForm = new FormGroup({
    // categoryId: new FormControl('', [Validators.required, Validators.pattern(this.regexForm)]),
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required])
    // ,
    // properties: new FormControl('')
  })

  constructor(private productService: ProductsService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) =>
    {this.uuid = params.get('uuid')!.toString()});
    this.productService.getProduct(this.uuid).subscribe((data: HttpResponse<Product>) => {
      console.log(data);
      this.product = data.body!;
      this.updateProductForm.setValue({
        // categoryId: this.product.categoryId,
        name: this.product.name,
        description: this.product.description
        // ,
        // properties: 'test'
      })
    });
  }


  updateProduct(): void {
    if (this.updateProductForm.valid) {
      const ProductDTO: object = {
        // "categoryId": this.updateProductForm.getRawValue().categoryId,
        "name": this.updateProductForm.getRawValue().name,
        "description": this.updateProductForm.getRawValue().description,
        "properties": {
          "test": "test" //fixme
        }
      }
      console.log(ProductDTO);
      this.productService.updateProduct(this.uuid, ProductDTO)
        .subscribe((result) => {
            if (result.status === 200) {
              this.router.navigate(['/']);
            }
          }
        )
    }
  }

  get categoryId() {
    return this.updateProductForm.get('categoryId');
  }

  get name() {
    return this.updateProductForm.get('name');
  }

  get description() {
    return this.updateProductForm.get('description');
  }

  get properties() {
    return this.updateProductForm.get('properties');
  }
}
