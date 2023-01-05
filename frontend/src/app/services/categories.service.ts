import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category, Field} from "../model/category";
import {environment} from "../../environments/environment";
import {Product} from "../model/Product";
import {CategoriesComponent} from "../components/categories/categories.component";

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  constructor(private readonly http: HttpClient) { }

  getCategories(): Observable<Category[]> {
      const url = `${environment.apiUrl}/category`;
      return this.http.get<Category[]>(url);
  }

  getCategory(uuid: string): Observable<HttpResponse<Category>> {
    return this.http.get<Category>(environment.apiUrl + "/category/" + uuid, {observe: 'response'});
  }

  createCategory(CategoryDTO: object): Observable<HttpResponse<Category>>  {
    return this.http.post<Category>(environment.apiUrl + "/category", CategoryDTO,
      {observe: 'response'})
  }

  updateCategory(uuid: string, CategoryDTO: object): Observable<HttpResponse<Category>> {
    return this.http.put<Category>(environment.apiUrl + "/category/" + uuid, CategoryDTO,
      { observe: 'response'})
  }

  deleteCategory(categoryID: string) {
    return this.http.delete(environment.apiUrl + "/category/" + categoryID, {observe: 'response'})
  }

  addField(uuid: string, FieldDTO: object): Observable<HttpResponse<Field>> {
    return this.http.put<Field>(environment.apiUrl + "/category/" + uuid + "/fields", FieldDTO,
      { observe: 'response'})
  }

  removeField(uuid: string){
    return this.http.delete(environment.apiUrl + "/category/fields/" + uuid, {observe: 'response'})
  }


}
