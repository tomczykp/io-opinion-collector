import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import { environment } from 'src/environments/environment';
import {Product} from "../model/Product";

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  public baseUrl = environment.apiUrl + '/products'

  constructor(private httpClient: HttpClient) {
  }

  public getProduct(uuid: string): Observable<HttpResponse<Product>> {
    return this.httpClient.get<Product>(this.baseUrl + '/' + uuid, {observe: 'response'});
  }

  public getProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(this.baseUrl);
  }

  public addProduct(ProductDTO: object): Observable<HttpResponse<Product>> {
    return this.httpClient.post<Product>(this.baseUrl, ProductDTO,
      {'headers': {'content-type': 'application/json'} ,observe: 'response'})
  }


  public deleteProduct(id: string): void {
    this.httpClient.delete(environment.apiUrl + '/products/' + id).subscribe(data => {
      console.log(data);
    });
  }
}
