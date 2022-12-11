import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { OC, ProductsService } from 'src/app/services/productsServices/products.service';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  adminColumns: string[] = ['productId', 'name', 'description', 'deleted', 'categoryId', 'action'];
  columns: string[] = ['productId', 'name', 'description', 'deleted', 'categoryId'];
  data: MatTableDataSource<OC.Product>;
  authenticated = false;
  products: OC.Product[];
  text: string;
  role: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  
  constructor(public productsService: ProductsService,
              public authService: AuthService ) { }

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

}
