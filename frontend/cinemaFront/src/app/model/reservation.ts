export interface Reservation {
    id: string;
    name: string;
    surname: string;
    value: string;
    screeningTimeId: string;
}

export class Reservation implements Reservation{}