import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Observable } from 'rxjs/Observable'
import { AuthenticationService } from './authentication.service';
import { Category } from '../Category';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import 'rxjs/add/operator/do';
import { Reminder } from '../reminder';

@Injectable()
export class ReminderService {

  token: string;
  reminders: Array<Reminder>;
  reminderSubject: BehaviorSubject<Array<Reminder>>
  userId: string;

  constructor(private httpClient: HttpClient, private authService: AuthenticationService) {
    this.token = this.authService.getBearerToken();
    this.userId = this.authService.getUserNameFromSession();
    this.reminders = [];
    this.reminderSubject = new BehaviorSubject(this.reminders);
    this.fetchRemindersFromServer();
  }

  fetchRemindersFromServer() {
    this.httpClient.get<Array<Reminder>>(`http://localhost:8081/api/v1/reminder/`, {
      headers: new HttpHeaders()
        .set('Authorization', `Bearer ${this.token}`)
    })
      .subscribe((res) => {
        this.reminders = res;
        this.reminderSubject.next(this.reminders);
      }, (error) => {
        this.reminderSubject.error(error);
      });
  }

  deleteReminder(reminderId) {
    return this.httpClient.delete(`http://localhost:8081/api/v1/reminder/${reminderId}`, {
      headers: new HttpHeaders()
      .set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    }).do(() => {
        const deleteReminder = this.reminders.findIndex(rem => rem.reminderId === reminderId);
        this.reminders.splice(deleteReminder, 1);
        this.reminderSubject.next(this.reminders);
    });
  }

  getreminders(): BehaviorSubject<Array<Reminder>> {
    return this.reminderSubject;
  }

  createReminder(reminder): Observable<Reminder>{
    reminder.reminderCreatedBy = this.userId;
    return this.httpClient.post<Reminder>('http://localhost:8081/api/v1/reminder', reminder, {
      headers: new HttpHeaders()
        .set('Authorization', `Bearer ${this.token}`)
    }).do((data) => {
        this.reminders.push(data);
        this.reminderSubject.next(this.reminders);
    })
  }

}
