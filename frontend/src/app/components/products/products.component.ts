import { Component, OnInit } from '@angular/core';
import {ProductsService} from "../../services/products.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Product, ProductFull} from "../../model/Product";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  product: ProductFull = <ProductFull>{};
  productsByConstantId: Array<ProductFull> = [];
  productHasProperties: boolean = false;
  regex: RegExp = new RegExp('[0-9a-f\-]+$');
  role: string;

  constructor(private productService: ProductsService, private router: Router, private activeRoute: ActivatedRoute) {
    this.activeRoute.params.subscribe(routeParams => {
      this.ngOnInit();
    });
  }

  ngOnInit() {
    let href = this.router.url;
    let match = href.match(this.regex);
    this.getProduct(match![0]);
    this.role = localStorage.getItem("role")!;
  }

  getProduct(uuid: string) {
    this.productService.getProductFull(uuid).subscribe((data: HttpResponse<ProductFull>) => {
      this.product = data.body!;
      this.product.createdAt = this.getDateStr(this.product.createdAt);
      if(this.role == 'ADMIN') {
        this.getProductHistory();
      }
      if(this.product.properties.length != 0) {
        this.productHasProperties = true;
      }
    })};

  getProductHistory() {
    this.productService.getProductsByConstantId(this.product.constantProductId)
      .subscribe(data => {
        this.productsByConstantId = data;
        this.productsByConstantId = this.productsByConstantId.sort(function (p1, p2) {
           return p1.createdAt < p2.createdAt ? 1 : -1;
        });
        this.productsByConstantId.forEach(prod => {
          prod.createdAt = this.getDateStr(prod.createdAt);
        })
      })
  }

  getDateStr(date: string) {
    return date.split('T').join(" ").split('.')[0];
  }
}
