import { Component, OnInit } from '@angular/core';
import { Note } from '../note';
import { NotesService } from '../services/notes.service';
import { Category } from '../Category';
import { CategoryService } from '../services/category.service';
import { ReminderService } from '../services/reminder.service';
import { Reminder } from '../reminder';

@Component({
  selector: 'app-note-taker',
  templateUrl: './note-taker.component.html',
  styleUrls: ['./note-taker.component.css']
})
export class NoteTakerComponent implements OnInit {

  note: Note;
  notes: Array<Note> = [];
  errMessage: string;
  categories: Array<Category> = [];
  reminders: Array<Reminder> = [];

  constructor(private noteService: NotesService, private categoryService: CategoryService, private reminderService: ReminderService) {
    this.note = new Note();
  }

  ngOnInit() {
    this.categoryService.getCategories().subscribe((data) => {
      this.categories = data;
    }, (err) => {
      this.errMessage = err.message;
    });
    this.reminderService.getreminders().subscribe((data) => {
      this.reminders = data;
    }, (err) => {
      if(err ! == null && err.status !== 404) {
        this.errMessage = err.message;
      }
      this.reminders = [];
    });
  }

  public addNote() {
    this.notes.push(this.note);
    
    if (this.note.title === '' || this.note.text === '') {
      this.errMessage = 'Title and Text both are required fields';
      return;
    }
    this.noteService.addNote(this.note).subscribe(
      () => {
        this.note = new Note();
      }, (err) => {
        const noteIndex = this.notes.findIndex(val => val.title === this.note.text);
        this.notes.splice(noteIndex, 1);
        this.errMessage = err.message;
        this.note = new Note();
      });
  }

  public deleteAllNotes() {
    const confirm = window.confirm("Do you really want to delete all the notes?");
    if (confirm) {
      this.noteService.deleteAllNote().subscribe((isDeleted) => {
        if (!isDeleted) {
          this.errMessage = 'Could not delete Note';
        }
      }, (err) => {
        this.errMessage = err.message;
      })
    }
  }
}
