import { Observable } from 'rxjs';
import { Room } from '../model/room';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { config } from '../../config'
import { Movie } from '../model/movie';

@Injectable({providedIn: 'root'})

export class MovieService{
  private apiServerUrl = config.apiBaseUrl;

  constructor(private http: HttpClient){}

  public getMovies(): Observable<Movie[]>{
    return this.http.get<Movie[]>(`${this.apiServerUrl}/movie/all`);
  }
}
