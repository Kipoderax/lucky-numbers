import {Component, OnInit} from '@angular/core';
import {GameService} from '../service/gameService';

@Component({
  selector: 'app-zaklady',
  templateUrl: './zaklady.component.html',
  styleUrls: ['./zaklady.component.css']
})
export class ZakladyComponent implements OnInit {

  constructor(
    private gameService: GameService
  ) { }

  ngOnInit() {
    this.gameService.outputBet().subscribe();
  }

}
