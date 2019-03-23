import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(
    private http: HttpClient
  ) { }

  outputBet() {
    return this.http.get('http://localhost:8080/zaklady');
  }
}
