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

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'products/:uuid', component: ProductsComponent},
  {
    path: 'dashboard', component: DashboardComponent,
    children: [
      {
        path: 'users', // child route path
        component: UsersComponent, // child route component that the router renders
      },
      {
        path: 'events',
        component: EventsComponent, // another child route component that the router renders
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
