import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category} from "../model/category";
import {environment} from "../../environments/environment";
import {Product} from "../model/Product";
import {CategoriesComponent} from "../components/categories/categories.component";

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  constructor(private readonly http: HttpClient) { }

  getCategories(): Observable<Category[]>
  {
      const url = `${environment.apiUrl}/category`;
      return this.http.get<Category[]>(url);
  }

  createCategory(CategoryDTO: object): Observable<HttpResponse<Category>>  {
    return this.http.post<Category>(environment.apiUrl + "/category", CategoryDTO,
      {observe: 'response'})
  }

  deleteCategory(categoryID: String) {
    return this.http.delete(environment.apiUrl + "/category/" + categoryID, {observe: 'response'})
  }
}
