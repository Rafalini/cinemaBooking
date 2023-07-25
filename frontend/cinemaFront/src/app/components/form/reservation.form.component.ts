import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Reservation } from 'src/app/model/reservation';
import { ReservationSeatList } from 'src/app/model/reservationSeatList';
import { ReservationStatus } from 'src/app/model/reservationStatus';
import { ReservationService } from 'src/app/services/reservation.service';
import { ScreeningTimeService } from 'src/app/services/screening.time.service';
import { config } from 'src/config';

@Component({
  selector: 'reservation-form',
  templateUrl: './reservation.form.component.html',
  styleUrls: ['./reservation.form.component.css']
})
export class FormComponent implements OnInit {

  constructor(
    private fb: FormBuilder,
    private screeningService: ScreeningTimeService,
    private reservationService: ReservationService,
    private router: Router,
    private route: ActivatedRoute) {
    this.zeroToTen = new Array(11).fill(0).map((x, i) => i);
    this.reservationForm = this.fb.group({
      name: ['', [
        Validators.required,
        Validators.pattern('[A-ZĄĘŁĆŃÓŚŹŻ][a-ząęłćńóśźż]+'),
        Validators.minLength(3)
      ]],
      surname: ['', [
        Validators.required,
        Validators.pattern('[A-ZĄĘŁĆŃÓŚŹŻ][a-ząęłćńóśźż]+(-[A-ZĄĘŁĆŃÓŚŹŻ][a-ząęłćńóśźż]+)?'),
        Validators.minLength(3)
      ]],
      studentsTckets: 0,
      adultTckets: 0,
      kidTckets: 0
    })
  }

  get name(){return this.reservationForm.get('name');}
  get surname(){return this.reservationForm.get('surname');}

  reservationStatus = ReservationStatus;
  config = config
  public zeroToTen: number[];
  public screening: any;
  public screeningId: any;
  public reservedSeats: any;
  public allSeats: any[] = [];
  public rows: number[] = [];
  public collumns: number[] = [];
  public usedTickets:number= 0;
  public availableTickets:number= 0;
  public reservationForm: FormGroup;
  public value:number=0;


  ngOnInit() {
    this.screeningId = this.route.snapshot.paramMap.get('id');
    this.getScreeningDetails(this.screeningId);
  }

  public getScreeningDetails(id: string): void {
    forkJoin([this.screeningService.getScreeningTimeById(id),
    this.reservationService.getSeats(this.screeningId)]).subscribe((result) => {
      this.screening = result[0]
      this.reservedSeats = result[1]

      let seatRows = this.screening.room.seatRows
      let seatsInRow = this.screening.room.seatsInRow

      this.rows = Array(seatRows).fill(0).map((x, i) => i + 1);
      this.collumns = Array(seatsInRow).fill(0).map((x, i) => i + 1);

      Array(seatRows * seatsInRow).fill(0).map((x, i) => i + 1).forEach((seatNumber) => {
        if (this.isReserved(seatNumber))
          this.allSeats.push({ "number": seatNumber, "status": ReservationStatus.Free })
        else
          this.allSeats.push({ "number": seatNumber, "status": ReservationStatus.Blocked })
      })

      console.log(this.allSeats)
    })
  }

  public isReserved(num: number): Boolean {
    const found = this.reservedSeats.find((obj: { seatNumber: number; }) => { return obj.seatNumber === num; });
    return found == undefined;
  }

  public reserveAction(num: number) {
    if (this.allSeats[num].status == ReservationStatus.Free)
      this.allSeats[num].status = ReservationStatus.Reserved
    else
      this.allSeats[num].status = ReservationStatus.Free

    this.usedTickets = this.allSeats.filter(x => x.status == ReservationStatus.Reserved).length
    this.availableTickets = Number(this.reservationForm.value.studentsTckets)
                          + Number(this.reservationForm.value.kidTckets)
                          + Number(this.reservationForm.value.adultTckets);
  }

  public isButtonVisible(num: number) {
    if (this.allSeats[num]?.status == ReservationStatus.Blocked)
      return false;
    else if (this.allSeats[num]?.status == ReservationStatus.Reserved)
      return true;
    else //if(this.allSeats[num]?.status == ReservationStatus.Free)
      return this.usedTickets < Number(this.reservationForm.value.studentsTckets)
        + Number(this.reservationForm.value.kidTckets)
        + Number(this.reservationForm.value.adultTckets)
  }

  public updateValue() {
    this.value = Number(this.reservationForm.value.studentsTckets) * config.studentTivketValue
      + Number(this.reservationForm.value.kidTckets) * config.kidTivketValue
      + Number(this.reservationForm.value.adultTckets) * config.adultTivketValue;
    this.availableTickets = Number(this.reservationForm.value.studentsTckets)
      + Number(this.reservationForm.value.kidTckets)
      + Number(this.reservationForm.value.adultTckets);
  }

  onSubmit(): void {
    const reservation:Reservation = new Reservation;
    reservation.name = this.reservationForm.value.name;
    reservation.surname = this.reservationForm.value.surname;
    reservation.value = String(this.value);
    reservation.screeningTimeId = this.screeningId;
    this.reservationService.saveReservation(reservation).subscribe((response) => {
      const reservationSeats = new ReservationSeatList;
      reservationSeats.reservationId = response.id;
      reservationSeats.reservedSeats = new Array();
      this.allSeats.forEach((seat) => {
        if(seat.status == ReservationStatus.Reserved)
          reservationSeats.reservedSeats.push(seat.number)
      })
      this.reservationService.saveSeats(reservationSeats).subscribe((resp) => {
        this.router.navigate(['/reservations-view']);
      });
    });   
  }
}
