import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Product} from "../../../model/Product";
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
    // category: this.fb.control('', Validators.required)
  })


  constructor(private productService: ProductsService,
              private categoryService: CategoriesService,
              private router: Router,
              private route: ActivatedRoute,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    // console.log(this.product.properties)
    this.route.paramMap.subscribe((params) => {
      this.uuid = params.get('uuid')!.toString()
    });

    // this.categoryService.getCategories().subscribe((data: Category[]) => {
    //   this.categories = data;
    // });

    this.productService.getProduct(this.uuid).subscribe((data: HttpResponse<Product>) => {
      this.product = data.body!;
      console.log(this.product.properties);
      let properties: {[id: string]: any} = this.product.properties;
      Object.keys(properties).forEach(key => {
        let value = properties[key];
        console.log(key, value);
        this.propertyKeys.push(key);
        this.updateProductForm.controls.propertiesValues.push(this.fb.control(
          value, [Validators.required]
        ));
      })

      this.updateProductForm.setValue({
        name: this.product.name,
        description: this.product.description,
        propertiesValues: this.product.properties,
        // category: this.categories[0].name
      })

      // this.loadProperties();
    });
  }

  updateProduct(): void {
    if (this.updateProductForm.valid) {
      const productDTO: object = {
        // "categoryId": this.updateProductForm.getRawValue().categoryId,
        "name": this.updateProductForm.getRawValue().name,
        "description": this.updateProductForm.getRawValue().description,
        "propertiesKeys": [
          'key1', 'key2'
        ],
        "propertiesValues": [
          'value1', 'value2'
        ]
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

  loadProperties() {
    // let keys = Object.keys(this.product.properties)
    // let values = Object.values(this.product.properties)
    // // let keyForm = this.fb.array([])
    // // let valueForm = this.fb.array([])
    // for (let i = 0; i < keys.length; i++) {
    // this.updateProductForm.controls.propertiesKeys.push(this.fb.control({keys[i], [Validators.required]}));
    // this.updateProductForm.controls.propertiesValues.push(this.fb.control({values[i], [Validators.required]}));
    //
    // }
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
