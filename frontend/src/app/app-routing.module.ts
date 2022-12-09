import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component"
import {EventsComponent} from "./components/events/events.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'events/admin-panel', component: EventsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
