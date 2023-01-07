import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {ProductsService } from 'src/app/services/products.service';
import {AuthService} from "../../services/auth.service";
import {Product} from "../../model/Product";
import {DeleteProductFormComponent} from "../products/delete-product-form/delete-product-form.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  adminColumns: string[] = ['productId', 'name', 'description', 'deleted', 'categoryId', 'action'];
  userColumns: string[] = ['productId', 'name', 'description', 'deleted', 'categoryId', 'makeUpdateForm', 'makeDeleteForm'];
  columns: string[] = ['productId', 'name', 'description', 'deleted', 'categoryId'];
  data: MatTableDataSource<Product>;
  authenticated = false;
  products: Product[];
  text: string;
  role: string;

  @Inject(DeleteProductFormComponent) productFormComponent: DeleteProductFormComponent
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(public productsService: ProductsService,
              public authService: AuthService) { }

  ngOnInit(): void {

    this.authService.authenticated.subscribe((change) => {
      this.authenticated = change;

      if (this.authenticated) {
        this.text = localStorage.getItem("email")!;
        this.role = localStorage.getItem("role")!;
      } else {
        this.text = "not logged in.";
      }
    })
  }

  ngAfterViewInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productsService.getProducts().subscribe(value => {
      this.products = value;

      this.data = new MatTableDataSource(this.products);
      this.data.paginator = this.paginator;
      this.data.sort = this.sort;
    })
  }

  deleteProduct(id: string): void {
    this.productsService.deleteProduct(id);
    location.reload();
  }

  makeDeleteForm(id: string) {
    this.productFormComponent.deleteProduct();
  }

}
