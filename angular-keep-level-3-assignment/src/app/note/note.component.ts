import { Component, Input, OnInit } from '@angular/core';
import { Note } from '../note';
import { RouterService } from '../services/router.service';
import { NotesService } from '../services/notes.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-note',
  templateUrl: './note.component.html',
  styleUrls: ['./note.component.css']
})
export class NoteComponent implements OnInit{

  errMessage: string;
  isCategoryNotNull: boolean;

  @Input() note: Note;
  constructor(private routerService: RouterService, private noteService: NotesService,private route: ActivatedRoute ) {
    this.note = new Note();
    this.isCategoryNotNull = false;

  }

  ngOnInit() {
    if(this.note.category != null) {
      this.isCategoryNotNull = true;
    }
  }

  editNote() {
    this.routerService.routeToEditNoteView(this.note.id);
  }

  deleteNote() {
    this.noteService.deleteNote(this.note.id).subscribe((isDeleted) => {
      if(!isDeleted) {
        this.errMessage = 'Could not delete Note';
      }
    })
  }

  setCardStyle() {
    let count = ((+(this.note.id)) * 4) % 3;
    let styles = {
      'background-color': count === 0? 'aliceblue' : (count === 1 ? 'aquamarine' : (count === 2? 'lightcyan' : 'aqua'))
    };
    return styles;
  }
}