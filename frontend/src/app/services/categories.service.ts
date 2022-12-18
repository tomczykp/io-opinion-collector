import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category} from "../model/category";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  constructor(private readonly httpClient: HttpClient) { }

  getCategories(): Observable<Category[]>
  {
      const url = `${environment.apiUrl}/category`;
      return this.httpClient.get<Category[]>(url);
  }
}
