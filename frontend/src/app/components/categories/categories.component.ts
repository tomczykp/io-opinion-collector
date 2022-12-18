import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {Opinion} from "../../model/Opinion";
import {Category} from "../../model/category";
import {CategoriesService} from "../../services/categories.service";

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html'
})
export class CategoriesComponent implements OnInit {

  categories$: Observable<Category[]>;
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

}
