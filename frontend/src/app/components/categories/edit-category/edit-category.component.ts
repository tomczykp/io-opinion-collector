import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {CategoriesService} from "../../../services/categories.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Category} from "../../../model/category";
import {HttpResponse} from "@angular/common/http";
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
    newFieldsTypes: this.fb.array([]),
    oldFieldsIds: this.fb.array([])
  })

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
          this.getOldFieldsIds.push(this.fb.control(this.category.fields[i].fieldID));
        }
        this.editCategoryForm.patchValue({
          name: this.category.name,
          parent: this.category.parentCategory?.categoryID
        })
      });
  }

  getOldFieldName(i : number) {
    return this.getOldFieldsNames.at(i);
  }
  getOldFieldType(i : number) {
    return this.getOldFieldsTypes.at(i);
  }
  getNewFieldName(i : number) {
    return this.getNewFieldsNames.at(i);
  }
  getNewFieldType(i : number) {
    return this.getNewFieldsTypes.at(i);
  }
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
  get getOldFieldsIds() {
    return this.editCategoryForm.get('oldFieldsIds') as FormArray;
  }
  addFieldsNames() {
    this.getNewFieldsNames.push(new FormControl('', [Validators.required]));
  }
  addFieldsTypes() {
    this.getNewFieldsTypes.push(new FormControl('String', [Validators.required]));
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
    private fb: FormBuilder

  ) {}


  removeNewFields(i : number){
    this.removeNewFieldsNames(i);
    this.removeNewFieldsTypes(i);
  }
  removeOldFields(i : number){
    if(confirm("Do you really want to delete this field? This action can't be undone")){
      this.categoriesService.removeField(this.getOldFieldsIds.getRawValue()[i])
        .subscribe((result) => {
          if (result.status === 200) {
            this.removeOldFieldsNames(i);
            this.removeOldFieldsTypes(i);
            this.getOldFieldsIds.removeAt(i);
          }})
    }
  }
  editCategory() {
    if (this.editCategoryForm.valid) {

      for(let i = 0;i < this.getOldFieldsTypes.getRawValue().length; i++){
        const FieldDTO: object = {
          "name": this.getOldFieldsNames.getRawValue()[i],
          "type": this.getOldFieldsTypes.getRawValue()[i]
        };
        this.categoriesService.updateField(this.getOldFieldsIds.getRawValue()[i], FieldDTO)
          .subscribe((result) => {
            if (result.status === 200) {}})}

      for(let i = 0;i < this.getNewFieldsTypes.getRawValue().length; i++){
        const FieldDTO: object = {
          "name": this.getNewFieldsNames.getRawValue()[i],
          "type": this.getNewFieldsTypes.getRawValue()[i]
        };
        this.categoriesService.addField(this.uuid, FieldDTO)
          .subscribe((result) => {if (result.status === 200) {
          }})
      }

      const CategoryDTO: object = {
        "name": this.editCategoryForm.getRawValue().name,
        "parentCategoryID": this.editCategoryForm.getRawValue().parent
      }
      this.categoriesService.updateCategory(this.uuid, CategoryDTO)
        .subscribe((result) => {
            if (result.status === 200) {
              this.router.navigate(['/dashboard/categories']);
            }
          }
        )
    }
  }
}
