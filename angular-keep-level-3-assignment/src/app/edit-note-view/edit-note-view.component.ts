import { Component, Inject } from '@angular/core';
import { Note } from '../note';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { NotesService } from '../services/notes.service';

@Component({
  selector: 'app-edit-note-view',
  templateUrl: './edit-note-view.component.html',
  styleUrls: ['./edit-note-view.component.css']
})
export class EditNoteViewComponent {
  note: Note;
  states: Array<string> = ['not-started', 'started', 'completed'];
  errMessage: string;
  id: number;

  constructor(
    @Inject(MAT_DIALOG_DATA) data, private noteService: NotesService,
    private dialogRef: MatDialogRef<EditNoteViewComponent>) {
    // Convert the noteId to number
    this.id = (+(data.noteId));
    this.note = this.noteService.getNoteById(this.id);
  }

  onSave() {
    this.noteService.editNote(this.note).subscribe(() => {
      this.dialogRef.close();
    }, (error) => {
      this.errMessage = error.message;
    });
  }
}
