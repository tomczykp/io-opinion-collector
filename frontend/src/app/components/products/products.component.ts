import { Component, OnInit } from '@angular/core';
import {ProductsService} from "../../services/products.service";
import {Router} from "@angular/router";
import {Product} from "../../model/Product";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  product: Product = <Product>{};
  regex: RegExp = new RegExp('[0-9a-f\-]+$');

  constructor(private productService: ProductsService, private router: Router) {

  }

  ngOnInit() {
    let href = this.router.url;
    let match = href.match(this.regex);
    this.getProduct(match![0]);
    console.log(match!.toString());
  }

  getProduct(uuid: string) {
    this.productService.getProduct(uuid).subscribe((data: HttpResponse<Product>) => {
      console.log(data)
      this.product = data.body!;
    })};

}
