import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormComponent } from './components/form/reservation.form.component';
import { HomePageComponent } from './components/home.page/home.page.component';
import { ReservationsViewComponent } from './components/reservations.view/reservations.view.component';

const routes: Routes = [
  { path: '', redirectTo: 'home-page', pathMatch: 'full'},
  { path: 'home-page', component: HomePageComponent},
  { path: 'reservation-form/:id', component: FormComponent},
  { path: 'reservations-view', component: ReservationsViewComponent},
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
