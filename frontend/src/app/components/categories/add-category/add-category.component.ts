import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ProductsService} from "../../../services/products.service";
import {Router} from "@angular/router";
import {CategoriesService} from "../../../services/categories.service";

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html'
})
export class AddCategoryComponent implements OnInit {
  regex: RegExp = new RegExp('^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$');

  addCategoryForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    fields: this.fb.array([
      this.fb.control('')
    ])
  })

  get getFields() {
    return this.addCategoryForm.get('fields') as FormArray;
  }
  addFields() {
    this.getFields.push(this.fb.control(''));
  }
  get name() {
    return this.addCategoryForm.get('name');
  }

  constructor(
    private categoriesService: CategoriesService,
    private router: Router,
    private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
  }

  createCategory() {
    if (this.addCategoryForm.valid) {
      const CategoryDTO: object = {
        "name": this.addCategoryForm.getRawValue().name
      }
    console.log(CategoryDTO);
    this.categoriesService.createCategory(CategoryDTO)
      .subscribe((result) => {
          if (result.status === 200) {
            this.router.navigate(['/']);
          }
        }
      )
    }
  }
}
