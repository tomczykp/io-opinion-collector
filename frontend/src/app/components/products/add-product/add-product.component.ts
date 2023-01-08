import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ProductsService} from "../../../services/products.service";
import {Category} from "../../../model/category";
import {CategoriesService} from "../../../services/categories.service";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html'
})
export class AddProductComponent implements OnInit {
  // regex: RegExp = new RegExp('^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$');
  patternValidate: RegExp = new RegExp('^( )*[^ ].*( )*$');

  categories: Category[];
  categoryId: string;
  propertyKeys: any = [];

  addProductForm = new FormGroup({
    category: this.fb.control('', [Validators.required, Validators.pattern(this.patternValidate)]),
    name: this.fb.control('', [Validators.required, Validators.pattern(this.patternValidate)]),
    description: this.fb.control('', [Validators.required, Validators.pattern(this.patternValidate), Validators.maxLength(4000)]),
    properties: this.fb.array([])
  })


  get name() {
    return this.addProductForm.get('name');
  }

  get description() {
    return this.addProductForm.get('description');
  }

  get properties() {
    return this.addProductForm.get('properties') as FormArray;
  }

  constructor(
    private productService: ProductsService,
    private categoryService: CategoriesService,
    private router: Router,
    private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe((data: Category[]) => {
      this.categories = data;
    });
  }

  addProduct() {
    if (this.addProductForm.valid) {
      let propertiesValues = this.addProductForm.getRawValue().properties;
      const pMap: Map<string, string> = new Map();
      for (let i = 0; i < propertiesValues.length; i++) {
        pMap.set(this.propertyKeys[i], propertiesValues[i] as string)
      }
      let properties;
      if(pMap.size == 0) {
        properties = null;
      } else {
        properties = Object.fromEntries(pMap);
      }
      const ProductDTO: object = {
        "categoryId": this.categoryId,
        "name": this.addProductForm.getRawValue().name,
        "description": this.addProductForm.getRawValue().description,
        "properties": properties
      }
      this.productService.addProduct(ProductDTO)
        .subscribe((result) => {
            if (result.status === 200) {
              this.router.navigate(['/']);
            }
          }
        )
    }
  }

  onCategoryChanged(value: string) {
    this.categoryId = value.split(' ')[1];
    let selectedCategory = this.categories.find(obj => {
      return obj.categoryID == this.categoryId;
    })
    if(selectedCategory === undefined) {
       return;
    }
    let properties: {[id: string]: any} = [];
    let mutableCategory: Category|null = selectedCategory;
    while(mutableCategory !== null) {
      mutableCategory.fields.forEach(field => {
        properties[field.name] = field.type;
      })
      mutableCategory = mutableCategory.parentCategory;
    }
    this.addProductForm.controls.properties.clear();
    this.propertyKeys = [];
    Object.keys(properties).forEach(key => {
      let value = properties[key];
      this.propertyKeys.push(key);
      this.addProductForm.controls.properties.push(this.fb.control(
        '', [Validators.required, Validators.pattern(this.patternValidate)]));
    });
  }
}
