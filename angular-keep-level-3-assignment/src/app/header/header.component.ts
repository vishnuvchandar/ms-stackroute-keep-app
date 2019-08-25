import { Component } from '@angular/core';
import { RouterService } from '../services/router.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  isNoteView = true;
  constructor(private routeService: RouterService) { }
  clicked() {
    if (this.isNoteView) {
      this.routeService.routeToListView();
      this.isNoteView = false;
    } else if (!this.isNoteView) {
      this.routeService.routeToNoteView();
      this.isNoteView = true;
    }
  }

}
