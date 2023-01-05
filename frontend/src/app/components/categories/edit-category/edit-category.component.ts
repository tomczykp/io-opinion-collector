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
    oldFieldsNames: this.fb.array([]),
    oldFieldsTypes: this.fb.array([]),
    newFieldsNames: this.fb.array([]),
    newFieldsTypes: this.fb.array([])
  })

  get getOldFieldsNames() {
    return this.editCategoryForm.get('oldFieldsNames') as FormArray;
  }
  get getOldFieldsTypes() {
    return this.editCategoryForm.get('oldFieldsTypes') as FormArray;
  }
  get getNewFieldsNames() {
    return this.editCategoryForm.get('newFieldsNames') as FormArray;
  }
  get getNewFieldsTypes() {
    return this.editCategoryForm.get('newFieldsTypes') as FormArray;
  }
  addFieldsNames() {
    this.getNewFieldsNames.push(this.fb.control(''));
  }
  addFieldsTypes() {
    this.getNewFieldsTypes.push(this.fb.control(''));
  }
  private removeOldFieldsNames(i : number) {
    this.getOldFieldsNames.removeAt(i);
  }
  private removeOldFieldsTypes(i : number) {
    this.getOldFieldsTypes.removeAt(i);
  }
  private removeNewFieldsNames(i : number) {
    this.getNewFieldsNames.removeAt(i);
  }
  private removeNewFieldsTypes(i : number) {
    this.getNewFieldsTypes.removeAt(i);
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
      this.category = data.body!;
      for(let i = 0;i < this.category.fields.length; i++){
        this.getOldFieldsNames.push(this.fb.control(this.category.fields[i].name));
        this.getOldFieldsTypes.push(this.fb.control(this.category.fields[i].type));
      }
      this.editCategoryForm.patchValue({
        name: this.category.name,
        parent: this.category.parentCategory?.categoryID
      })
    });
  }

  removeNewFields(i : number){
    this.removeNewFieldsNames(i);
    this.removeNewFieldsTypes(i);
  }
  removeOldFields(i : number){
    this.removeOldFieldsNames(i);
    this.removeOldFieldsTypes(i);
  }

  editCategory() {
    if (this.editCategoryForm.valid) {
      for(let i = 0;i < this.getNewFieldsTypes.getRawValue().length; i++){
        const FieldDTO: object = {
          "name": this.getNewFieldsNames.getRawValue()[i],
          "type": this.getNewFieldsTypes.getRawValue()[i]
        };
        this.categoriesService.addField(this.uuid, FieldDTO)
          .subscribe((result) => {
              if (result.status === 200) {
                this.router.navigate(['/']);
              }
            }
          )
      }
      for(let i = 0;i < this.getOldFieldsTypes.getRawValue().length; i++){
        const FieldDTO: object = {
          "name": this.getOldFieldsNames.getRawValue()[i],
          "type": this.getOldFieldsTypes.getRawValue()[i]
        };
        this.categoriesService.updateField(this.category.fields[i].fieldID, FieldDTO)
          .subscribe((result) => {
              if (result.status === 200) {
                this.router.navigate(['/']);
              }
            }
          )
      }
      //for(let i = 0; i < this.category.fields.length ; i++){
        //this.removeFields();
        //console.log(this.category.fields[i].fieldID);
        //this.categoriesService.removeField(this.category.fields[i].fieldID).subscribe()
      //}
      const CategoryDTO: object = {
        "name": this.editCategoryForm.getRawValue().name,
        "parentCategoryID": this.editCategoryForm.getRawValue().parent
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
