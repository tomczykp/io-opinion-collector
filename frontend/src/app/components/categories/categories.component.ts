import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {Observable} from "rxjs";
import {Category} from "../../model/category";
import {CategoriesService} from "../../services/categories.service";
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import * as _ from "underscore";

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html'
})
export class CategoriesComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['name', 'categoryID', 'parentCategory', 'fields', 'edit', 'delete'];

  dataSource: MatTableDataSource<Category>;
  @ViewChild('paginator') paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  categories: Category[];
  nameFilter = "";
  addCategoryForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    //fields: new FormArray();
  })
  constructor(
    private categoriesService : CategoriesService
  ) {}

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.getCategories();
  }

  getCategories() {
    this.categoriesService.getCategories().subscribe((categories) => {
      this.categories = categories.filter((category) => {
        return category.name.includes(this.nameFilter);
      });
      this.categories = _.sortBy(this.categories, 'name');
      this.dataSource = new MatTableDataSource(this.categories);
      this.dataSource.paginator = this.paginator;
    })
  }
  deleteCategory(categoryID: string) {
    if (confirm("Do you really want to delete category with id: " + categoryID + "? This action can't be undone")) {
      this.categoriesService.deleteCategory(categoryID).subscribe((result) => {
        this.getCategories();
      });
    }
  }

  clearFilter() {
    this.nameFilter = "";
    this.getCategories();
  }

}
