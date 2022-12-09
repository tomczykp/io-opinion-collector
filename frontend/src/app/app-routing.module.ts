import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {RegisterComponent} from "./components/register/register.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {EventsComponent} from "./components/events/events.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'events/admin-panel', component: EventsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
