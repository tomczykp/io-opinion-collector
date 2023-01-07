import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from 'src/environments/environment';
import {Product, ProductDF, ProductFull} from "../model/Product";

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

  public getProductFull(uuid: string): Observable<HttpResponse<ProductFull>> {
    return this.httpClient.get<ProductFull>(this.baseUrl + '/' + uuid, {observe: 'response'});
  }

	public getProducts(): Observable<ProductFull[]> {
		return this.httpClient.get<ProductFull[]>(this.baseUrl)
	}

  public addProduct(ProductDTO: object): Observable<HttpResponse<Product>> {
    return this.httpClient.post<Product>(this.baseUrl + '/' + "suggestion", ProductDTO,
      {'headers': {'content-type': 'application/json'}, observe: 'response'})
  }

  public updateProduct(uuid: string, ProductDTO: object): Observable<HttpResponse<Product>> {
    return this.httpClient.put<Product>(this.baseUrl + '/' + uuid, ProductDTO,
      {'headers': {'content-type': 'application/json'}, observe: 'response'})
  }

  public deleteProduct(id: string): void {
    this.httpClient.delete(this.baseUrl + '/' + id).subscribe(data => {
      console.log(data);
    });
  }

  public makeDeleteForm(id: string, formDF: object): Observable<HttpResponse<ProductDF>> {
    return this.httpClient.put<ProductDF>(this.baseUrl + '/' + id + '/' + 'delete', formDF,
      {'headers': {'content-type': 'application/json'}, observe: 'response'})
  }
}
