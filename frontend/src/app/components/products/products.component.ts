import { Component, OnInit } from '@angular/core';
import {ProductsService, OC} from "../../services/productsServices/products.service";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  product: OC.Product;

  constructor(private productService: ProductsService) { }

  ngOnInit() {
  }

  getProduct(uuid: string) {
    this.productService.getProduct(uuid).subscribe((data: OC.Product) => {
      console.log(data)
      this.product = data;
    })};

}
