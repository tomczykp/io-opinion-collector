import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Observable} from "rxjs";
import {Opinion} from "../../model/Opinion";
import {Category} from "../../model/category";
import {CategoriesService} from "../../services/categories.service";
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatTableDataSource} from "@angular/material/table";
import {User} from "../../model/User";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html'
})
export class CategoriesComponent implements OnInit {

  dataSource: MatTableDataSource<Category>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  categories$: Observable<Category[]>;

  addCategoryForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    //fields: new FormArray();
  })
  constructor(
    private categoriesService : CategoriesService
  ) {}

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories()
  {
    this.categories$ = this.categoriesService.getCategories();
  }

  deleteCategory(categoryID: string) {
    if (confirm("Do you really want to delete category with id: " + categoryID + "? This action can't be undone")) {
      this.categoriesService.deleteCategory(categoryID).subscribe((result) => {
        this.getCategories();
      });
    }
  }

}
