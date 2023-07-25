import { Observable } from 'rxjs';
import { Room } from '../model/room';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { config } from '../../config'

@Injectable({providedIn: 'root'})

export class RoomService{
  private apiServerUrl = config.apiBaseUrl;

  constructor(private http: HttpClient){}

  public getRooms(): Observable<Room[]>{
    return this.http.get<Room[]>(`${this.apiServerUrl}/room/all`);
  }

  public addRooms(room: Room): Observable<Room>{
    return this.http.post<Room>(`${this.apiServerUrl}/room/add`, room);
  }
}
