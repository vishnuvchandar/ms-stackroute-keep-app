import { Component, OnInit } from '@angular/core';
import { Note } from '../note';
import { NotesService } from '../services/notes.service';

@Component({
  selector: 'app-note-view',
  templateUrl: './note-view.component.html',
  styleUrls: ['./note-view.component.css']
})
export class NoteViewComponent implements OnInit {

  errMessage: string;
  note: Note;
  notes: Array<Note> = [];
  constructor(private noteService: NotesService) {
    this.note = new Note();
  }

  ngOnInit() {
    this.noteService.getNotes().subscribe((data) => {
      this.notes = data;
    }, (err) => {
      this.errMessage = err.message;
    });
  }
}
