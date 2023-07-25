import { Component, OnInit } from '@angular/core';
import { Reservation } from 'src/app/model/reservation';
import { ReservationService } from 'src/app/services/reservation.service';
import { config } from 'src/config';

@Component({
  selector: 'reservations-view',
  templateUrl: './reservations.view.component.html',
  styleUrls: ['./reservations.view.component.css']
})
export class ReservationsViewComponent implements OnInit {

  constructor(
    private reservationService: ReservationService,
  ) { }
  
  reservations: any[] = [];

  ngOnInit() {
    this.getReservations();
  }

  public getReservations() {
    this.reservations = Array()
    this.reservationService.getReservations().subscribe((response) => {
      response.forEach((reservation) => {

        if(reservation.url != null)
          reservation.url = reservation.url;

        console.log(reservation)
        
        this.reservations.push(reservation)
      })
    })
  }

  public confirmReservation(path:string){
    this.reservationService.confirmReservation(path).subscribe((response)=>{
      this.getReservations();
    }) 
  }

}
