import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  public baseUrl = 'http://localhost:8080/api/products'

  constructor(private httpClient: HttpClient) {
  }

  public getProduct(uuid: string): Observable<OC.Product> {
    return this.httpClient
      .get<OC.Product>(this.baseUrl + '/' + uuid);
  }

}
// // Error handling
//   handleError(error: any) {
//     let errorMessage = '';
//     if (error.error instanceof ErrorEvent) {
//       // Get client-side error
//       errorMessage = error.error.message;
//     } else {
//       // Get server-side error
//       errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
//     }
//     window.alert(errorMessage);
//     return throwError(() => {
//       return errorMessage;
//     });
//   }
// }


export namespace OC {
  export interface Product {
    productId: string,
    categoryId: string,
    name: string,
    description: string,
    deleted: boolean,
    confirmed: boolean,
    properties: Array<String> [];
  }
}
