import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Note } from '../note';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthenticationService } from './authentication.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import 'rxjs/add/operator/do';

@Injectable()
export class NotesService {

  notes: Array<Note>;
  notesSubject: BehaviorSubject<Array<Note>>;
  token: string;
  userId: string;
  constructor(private http: HttpClient, private authService: AuthenticationService) {
    this.token = this.authService.getBearerToken();
    this.userId = this.authService.getUserNameFromSession();
    this.notes = [];
    this.notesSubject = new BehaviorSubject(this.notes);
    this.fetchNotesFromServer();
  }

  fetchNotesFromServer() {

    this.http.get<Array<Note>>(`http://localhost:8082/api/v1/note/${this.userId}`, {
      headers: new HttpHeaders()
        .set('Authorization', `Bearer ${this.token}`)
    })
      .subscribe((res) => {
        this.notes = res;
        // noteSubject is populated with the array of notes
        this.notesSubject.next(this.notes);
      }, (error) => {
        this.notesSubject.error(error);
      });
  }

  getNotes(): BehaviorSubject<Array<Note>> {
    return this.notesSubject;
  }

  addNote(note: Note): Observable<Note> {
    note.noteCreatedBy = this.userId;
    return this.http.post<Note>('http://localhost:8082/api/v1/note', note, {
      headers: new HttpHeaders()
        .set('Authorization', `Bearer ${this.token}`)
    })
      .do((data) => {
        this.notes.push(data);
        this.notesSubject.next(this.notes);
      });
  }

  editNote(note: Note): Observable<Note> {
    return this.http.put<Note>(`http://localhost:8082/api/v1/note/${this.userId}/${note.id}`, note, {
      headers: new HttpHeaders()
        .set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    })
      .do((data) => {
        const updated = this.notes.find((edittedNote) => edittedNote.id === data.id);
        Object.assign(updated, data);
        this.notesSubject.next(this.notes);
      });
  }

  deleteNote(noteId) {
    return this.http.delete(`http://localhost:8082/api/v1/note/${this.userId}/${noteId}`, {
      headers: new HttpHeaders()
      .set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    }).do((res) => {
      if(res['isDeleted']) {
        const deletedNote = this.notes.findIndex((note) => note.id === noteId);
        this.notes.splice(deletedNote, 1);
        this.notesSubject.next(this.notes);
      }
        return res['isDeleted'];
    });
  }

  deleteAllNote() {
    return this.http.delete(`http://localhost:8082/api/v1/note/${this.userId}/`, {
      headers: new HttpHeaders()
      .set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    }).do((res) => {
      if(res['isDeleted']) {
        this.notes = [];
        this.notesSubject.next(this.notes);
      }
        return res['isDeleted'];
    });
  }

  getNoteById(noteId): Note {
    const ammended = this.notes.find((note) => note.id === noteId);
    return Object.assign({}, ammended);
  }
}
