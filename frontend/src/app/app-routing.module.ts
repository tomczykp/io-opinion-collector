import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {RegisterComponent} from "./components/register/register.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {AdminEventsDashboardComponent} from "./components/admin-events-dashboard/admin-events-dashboard.component";
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
import {EditCategoryComponent} from "./components/categories/edit-category/edit-category.component";
import {UserEventsDashboardComponent} from "./components/user-events-dashboard/user-events-dashboard.component";
import { InfoComponent } from './components/info/info.component';


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

  //Categories
  {path: 'categories/add', component: AddCategoryComponent},
  {path: 'categories/edit/:uuid', component: EditCategoryComponent},

  {path: 'user-events', component: UserEventsDashboardComponent},
  {path: 'info', component: InfoComponent},
  {
    path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard, AdminGuard],
    children: [
      {
        path: 'users',
        component: UsersComponent,
      },
      {
        path: 'events',
        component: AdminEventsDashboardComponent,
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
