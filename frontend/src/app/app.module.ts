import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {EventsComponent} from './components/events/events.component';
import {HttpClientModule} from "@angular/common/http";
import {MatTableModule} from "@angular/material/table";
import {MatSortModule} from "@angular/material/sort";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from "@angular/material/button";
import {MatPaginatorModule} from "@angular/material/paginator";
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ProductsComponent} from './components/products/products.component';
import {HomeComponent} from './components/home/home.component';
import {NavComponent} from './components/nav/nav.component';
import {MatIconModule} from "@angular/material/icon";

@NgModule({
  declarations: [
    AppComponent,
    EventsComponent,
    HomeComponent,
    NavComponent,
    ProductsComponent,


  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,

    // Material frontend
    MatTableModule,
    MatSortModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatPaginatorModule,

    NgbModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    MatIconModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
