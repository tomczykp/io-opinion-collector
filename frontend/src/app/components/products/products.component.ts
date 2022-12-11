import { Component, OnInit } from '@angular/core';
import {ProductsService, OC} from "../../services/productsServices/products.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  product: OC.Product = <OC.Product>{};
  regex: RegExp = new RegExp('[0-9a-f\-]+$');

  constructor(private productService: ProductsService, private router: Router) {

  }

  ngOnInit() {
    let href = this.router.url;
    let match = href.match(this.regex);
    this.getProduct(match![0]);
    console.log(match);
  }

  getProduct(uuid: string) {
    this.productService.getProduct(uuid).subscribe((data: OC.Product) => {
      console.log(data)
      this.product = data;
    })};

}
