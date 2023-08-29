import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { config } from '../../config'
import { Reservation } from '../model/reservation';
import { ReservationSeatList } from '../model/reservationSeatList';

@Injectable({providedIn: 'root'})

export class ReservationService{
  private apiServerUrl = config.apiBaseUrl;

  constructor(private http: HttpClient){}

  public confirmReservation(path:string): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiServerUrl}/reservation`+path);
  }

  public getSeats(id:string): Observable<any[]>{
    console.log(`${this.apiServerUrl}/reservation/seats/`+id)
    return this.http.get<any[]>(`${this.apiServerUrl}/reservation/seats/`+id);
  }

  public getReservations(): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiServerUrl}/reservation/all`);
  }

  public saveReservation(reservation:Reservation): Observable<Reservation>{
    return this.http.post<Reservation>(`${this.apiServerUrl}/reservation/add`, reservation);
  }

  public saveSeats(reservationSeats:ReservationSeatList): Observable<any[]>{
    return this.http.post<any[]>(`${this.apiServerUrl}/reservation/add-seats/`, reservationSeats);
  }
}
