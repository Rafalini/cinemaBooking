import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { config } from '../../config'
import { ScreeningTime } from '../model/screening.time';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ScreeningTimeService {

  private apiServerUrl = config.apiBaseUrl;

  constructor(private http: HttpClient){}

  public getScreeningTimes(date: string): Observable<ScreeningTime[]>{
    return this.http.get<ScreeningTime[]>(`${this.apiServerUrl}/screening/all`, {params:{"date" : date}});
  }

  public getScreeningTimeById(id: string): Observable<ScreeningTime[]>{
    return this.http.get<ScreeningTime[]>(`${this.apiServerUrl}/screening/`+id);
  }
}
