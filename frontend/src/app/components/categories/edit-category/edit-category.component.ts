import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {CategoriesService} from "../../../services/categories.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Category} from "../../../model/category";
import {HttpResponse} from "@angular/common/http";
import {Product} from "../../../model/Product";
import {Observable} from "rxjs";

@Component({
  selector: 'app-edit-category',
  templateUrl: './edit-category.component.html'
})
export class EditCategoryComponent implements OnInit {
  regex: RegExp = new RegExp('^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$');

  categories: Observable<Category[]>;
  category: Category = <Category>{};
  uuid: string;
  editCategoryForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    parent: new FormControl(''),
    fieldsNames: this.fb.array([]),
    fieldsTypes: this.fb.array([])
  })

  get getFieldsNames() {
    return this.editCategoryForm.get('fieldsNames') as FormArray;
  }
  addFieldsNames() {
    this.getFieldsNames.push(this.fb.control(''));
  }
  removeFieldsNames(i : number) {
    this.getFieldsNames.removeAt(i);
  }
  get getFieldsTypes() {
    return this.editCategoryForm.get('fieldsTypes') as FormArray;
  }
  addFieldsTypes() {
    this.getFieldsTypes.push(this.fb.control(''));
  }
  removeFieldsTypes(i : number) {
    this.getFieldsTypes.removeAt(i);
  }
  get name() {
    return this.editCategoryForm.get('name');
  }
  get parent() {
    //console.log(this.editCategoryForm.get('parent')?.getRawValue());
    return this.editCategoryForm.get('parent')?.getRawValue();
  }
  getCategories(){
    this.categories = this.categoriesService.getCategories();
  }
  addFields(){
    this.addFieldsNames();
    this.addFieldsTypes();
  }
  constructor(
    private categoriesService: CategoriesService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,

  ) {}

  ngOnInit(): void {
    this.getCategories();
    this.route.paramMap.subscribe((params) =>
    {this.uuid = params.get('uuid')!.toString()});
    this.categoriesService.getCategory(this.uuid).subscribe(
      (data: HttpResponse<Category>) => {
      //console.log(data);
      this.category = data.body!;
      for(let i = 0;i < this.category.fields.length; i++){
        this.getFieldsNames.push(this.fb.control(this.category.fields[i].name));
        this.getFieldsTypes.push(this.fb.control(this.category.fields[i].type));
      }
      this.editCategoryForm.patchValue({
        name: this.category.name,
        parent: this.category.parentCategory?.categoryID
      })
    });
  }

  removeFields(i : number){
    this.removeFieldsNames(i);
    this.removeFieldsTypes(i);
  }

  editCategory() {
    if (this.editCategoryForm.valid) {
      let fields : Array<object> = [];
      for(let i = 0;i<this.getFieldsNames.getRawValue().length; i++){
        const FieldDTO: object = {
          "name": this.getFieldsNames.getRawValue()[i],
          "type": this.getFieldsTypes.getRawValue()[i]
        };
        fields.push(FieldDTO);
      }

      console.log(fields.length);

      for(let i = 0; i < this.category.fields.length ; i++){
        //this.removeFields();
        //console.log(this.category.fields[i].fieldID);
        //this.categoriesService.removeField(this.category.fields[i].fieldID).subscribe()
      }
      for(let i = 0; i < fields.length; i++){
        console.log(fields[i]);
        this.categoriesService.addField(this.uuid, fields[i])
          .subscribe((result) => {
              if (result.status === 200) {
                this.router.navigate(['/']);
              }
            }
          )
      }

      const CategoryDTO: object = {
        "name": this.editCategoryForm.getRawValue().name,
        "parentCategoryID": this.editCategoryForm.getRawValue().parent,
        "fields": fields
      }
      console.log(CategoryDTO);
      this.categoriesService.updateCategory(this.uuid,CategoryDTO)
        .subscribe((result) => {
            if (result.status === 200) {
              this.router.navigate(['/']);
            }
          }
        )
    }
  }
}
