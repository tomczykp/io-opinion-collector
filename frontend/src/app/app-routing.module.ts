import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {RegisterComponent} from "./components/register/register.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {EventsComponent} from "./components/events/events.component";
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ResetComponent } from './components/reset/reset.component';
import { ResetConfirmComponent } from './components/reset/reset-confirm/reset-confirm.component';
import {UsersComponent} from "./components/users/users.component";
import {ProductsComponent} from "./components/products/products.component";
import {CategoriesComponent} from "./components/categories/categories.component";
import {AuthGuard} from "./guards/auth.guard";
import {AdminGuard} from "./guards/admin.guard";
import {LoginRegisterGuard} from "./guards/login-register.guard";
import {AddProductComponent} from "./components/products/add-product/add-product.component";
import {UpdateProductComponent} from "./components/products/update-product/update-product.component";
import {DeleteProductFormComponent} from "./components/products/delete-product-form/delete-product-form.component";
import {AddCategoryComponent} from "./components/categories/add-category/add-category.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent, canActivate: [LoginRegisterGuard]},
  {path: 'register', component: RegisterComponent, canActivate: [LoginRegisterGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  //Products
  {path: 'products/:uuid', component: ProductsComponent},
  {path: 'products/:uuid/update', component: UpdateProductComponent},
  {path: 'products/:uuid/delete', component: DeleteProductFormComponent},
  {path: 'suggestion', component: AddProductComponent},
  {path: 'categories/add', component: AddCategoryComponent},
  {
    path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard, AdminGuard],
    children: [
      {
        path: 'users',
        component: UsersComponent,
      },
      {
        path: 'events',
        component: EventsComponent,
      },
      {
        path: 'categories',
        component: CategoriesComponent
      },
    ]
  },
  {path: 'reset', component: ResetComponent},
  {path: 'resetConfirm/:token', component: ResetConfirmComponent},
  {path: '**', redirectTo: ''} // non existent path ---> home, later maybe 404 page
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
