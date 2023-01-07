import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {CategoriesService} from "../../../services/categories.service";
import {Observable} from "rxjs";
import {Category} from "../../../model/category";


@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html'
})
export class AddCategoryComponent implements OnInit {
  regex: RegExp = new RegExp('^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$');
  categories$: Observable<Category[]>;
  addCategoryForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    parent: new FormControl(''),
    fieldsNames: this.fb.array([]),
    fieldsTypes: this.fb.array([])
  })

  getFieldName(i : number) {
    return this.getFieldsNames.at(i);
  }
  getFieldType(i : number) {
    return this.getFieldsTypes.at(i);
  }

  get getFieldsNames() {
    return this.addCategoryForm.get('fieldsNames') as FormArray;
  }
  addFieldsNames() {
    this.getFieldsNames.push(new FormControl('', [Validators.required]));
  }
  removeFieldsNames(i : number) {
    this.getFieldsNames.removeAt(i);
  }
  get getFieldsTypes() {
    return this.addCategoryForm.get('fieldsTypes') as FormArray;
  }
  addFieldsTypes() {
    this.getFieldsTypes.push(new FormControl('Int', [Validators.required]));
  }
  removeFieldsTypes(i : number) {
    this.getFieldsTypes.removeAt(i);
  }
  get name() {
    return this.addCategoryForm.get('name');
  }

  addFields() {
    this.addFieldsNames();
    this.addFieldsTypes();
  }
  removeFields(i : number) {
    this.removeFieldsNames(i);
    this.removeFieldsTypes(i);
  }

  constructor(
    private categoriesService: CategoriesService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories(){
    this.categories$ = this.categoriesService.getCategories();
  }

  createCategory() {
    if (this.addCategoryForm.valid) {
      let fields : Array<object> = [];
      for(let i = 0;i<this.getFieldsNames.getRawValue().length; i++){
        const FieldDTO: object = {
          "name": this.getFieldsNames.getRawValue()[i],
          "type": this.getFieldsTypes.getRawValue()[i]
        };
        fields.push(FieldDTO);
      }
      console.log(fields);

      const CategoryDTO: object = {
        "name": this.addCategoryForm.getRawValue().name,
        "parentCategoryID": this.addCategoryForm.getRawValue().parent,
        "fields": fields
      }
    console.log(CategoryDTO);
    this.categoriesService.createCategory(CategoryDTO)
      .subscribe((result) => {
          if (result.status === 200) {
            this.router.navigate(['/dashboard/categories']);
          }
        }
      )
    }
  }
}
