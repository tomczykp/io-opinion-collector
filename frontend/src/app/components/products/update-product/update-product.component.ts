import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Product, ProductFull} from "../../../model/Product";
import {ProductsService} from "../../../services/products.service";
import {CategoriesService} from "../../../services/categories.service";
import {HttpResponse} from "@angular/common/http";
import {Category} from "../../../model/category";


@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html'
})
export class UpdateProductComponent implements OnInit {
  product: Product = <Product>{};
  // categories: Category[];
  propertyKeys: any = [];
  match: RegExpMatchArray | null;
  uuid: string;

  regexForm: RegExp = new RegExp('^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$');
  regexGet: RegExp = new RegExp('[0-9a-f\-]+$');


  updateProductForm = this.fb.group({
    name: this.fb.control('', [Validators.required]),
    description: this.fb.control('', [Validators.required]),
    propertiesValues: this.fb.array([]),
  })


  constructor(private productService: ProductsService,
              private categoryService: CategoriesService,
              private router: Router,
              private route: ActivatedRoute,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {

    this.route.paramMap.subscribe((params) => {
      this.uuid = params.get('uuid')!.toString()
    });

    this.productService.getProductFull(this.uuid).subscribe((data: HttpResponse<ProductFull>) => {
      let apiProd = data.body!;
      this.product = {
        productId: apiProd.productId,
        name: apiProd.name,
        description: apiProd.description,
        properties: apiProd.properties,
        deleted: apiProd.deleted,
        confirmed: apiProd.confirmed,
        categoryId: apiProd.category.categoryID
      } as Product;

      let properties: {[id: string]: any} = this.product.properties;
      Object.keys(properties).forEach(key => {
        let value = properties[key];
        this.propertyKeys.push(key);
        this.updateProductForm.controls.propertiesValues.push(this.fb.control(
          value, [Validators.required]
        ));
      })

      this.updateProductForm.setValue({
        name: this.product.name,
        description: this.product.description,
        propertiesValues: this.product.properties,
      })

    });
  }

  updateProduct(): void {
    if (this.updateProductForm.valid) {

      let propertiesValues = this.updateProductForm.getRawValue().propertiesValues.toString();
      const pMap: Map<string, string> = new Map()
      for (let i = 0; i < propertiesValues.length; i++) {
        pMap.set(this.propertyKeys[i], propertiesValues[i])
      }
      const productDTO = {
        "categoryId": this.product.categoryId,
        "name": this.updateProductForm.getRawValue().name,
        "description": this.updateProductForm.getRawValue().description,
        "properties": pMap
      }
      this.productService.updateProduct(this.uuid, productDTO)
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

  get propertiesValues() {
    return this.updateProductForm.get('propertiesValues') as FormArray;
  }
}
