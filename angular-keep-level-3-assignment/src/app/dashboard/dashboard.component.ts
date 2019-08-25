import { Component } from '@angular/core';
import { NotesService } from '../services/notes.service';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  constructor(private notesService: NotesService, private fb: FormBuilder) {
    this.notesService.fetchNotesFromServer();
  }

}
