import { Component, OnInit } from '@angular/core';
import { ScreeningTimeService } from 'src/app/services/screening.time.service';
import { Movie } from 'src/app/model/movie';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';


@Component({
  selector: 'app-root',
  templateUrl: './home.page.component.html',
  styleUrls: ['./home.page.component.css'],
})
export class HomePageComponent implements OnInit {
  public screenings: any[] = [];
  start_time: Date | undefined;

  constructor(
    private screeningService: ScreeningTimeService,
    private datePipe: DatePipe,
    private router: Router
  ) { }

  ngOnInit() {
    this.start_time = new Date();
    var date = this.datePipe.transform(this.start_time, "yyyy-MM-dd")
    if (date != null)
      this.screeningService.getScreeningTimes(date).subscribe(
        (response: any[]) => {
          this.loadScreenings(response)
        }
      );
  }

  public onDateChange(event: MatDatepickerInputEvent<Date>): void {
    var date = this.datePipe.transform(event.value, "yyyy-MM-dd")
    console.log(date)
    if (date != null)
      this.screeningService.getScreeningTimes(date).subscribe(
        (response: any[]) => {
          console.log(response)
          this.loadScreenings(response)
        }
      );
  }

  public loadScreenings(response: any){
    this.screenings = Array()
    if (response.length > 0) {
      var currentMovie = response[0].movie
      var localScreenings: any[] = []

      response.forEach((screening: any) => {

        var date = this.datePipe.transform(screening.screeningTime, "HH:mm")

        if (currentMovie.title != screening.movie.title) {
          this.screenings.push({ "title": currentMovie.title, "short":currentMovie.shortTitle, "screenings": localScreenings })
          currentMovie = screening.movie
          localScreenings = new Array()
          localScreenings.push({ "date": date, "id": screening.id })
        } else {
          if (date != null)
            localScreenings.push({ "date": date, "id": screening.id })
        }
      })
      this.screenings.push({ "title": currentMovie.title, "short":currentMovie.shortTitle, "screenings": localScreenings })
      console.log(this.screenings)
    }
  }

  public startReservation(id:string){
    this.router.navigate(['/reservation-form/'+id]);
  }
}
