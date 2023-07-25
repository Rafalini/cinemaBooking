# Cinema Ticket Booking App

## Contents:
### Backend
Spring based app, managing requests and connection with database.
### Frontend
Angular based front-end app.
## Minimal requirements
* npm - 8.19.3
* ng - 16.1.5
* java - 17

Tested on Ubuntu 21.04 & MySQL database.

## How to run
1. Configure database source in ```ticketBooking/backend/cinema/src/resources/application.properties```.
2. To run backend app go to ```ticketBooking/backend/cinema``` and run:
```
mvn spring-boot:run
```
Once backend is running, it can be filled with demo data. To do so run ```ticketBooking/fillDatabase.sh``` (adding permission for execution might be nessesary)
```
ticketBooking/:$ chomd +x ./fillDatabase.sh
ticketBooking/:$ ./fillDatabase.sh
```
Script will fill database with example room's, movie's and screening's.

3. To start frontend go to ```ticketBooking/frontend/cinemaFront``` and run:
```
npm install
npm install @angular/cli
ng serve
```

4. Go to ```http://localhost:4200``` and test the app.

### Tests

To run tests use: ```mvn tests``` or ```mvn install```
However, it is required to use clean database for tests (ideally separate database), because new entities are created with fixed ID's (which must be unused). Also it is recommended not to use database after tests, because its lacking some attributes.


### PostMan collection
In project is also included [PostMan](https://www.postman.com/) collection, for manual endpoint testing. 
