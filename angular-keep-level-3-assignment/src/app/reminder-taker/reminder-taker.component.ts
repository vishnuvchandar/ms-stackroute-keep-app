import { Component, OnInit } from '@angular/core';
import { Reminder } from '../reminder';
import { ReminderService } from '../services/reminder.service';

@Component({
  selector: 'app-reminder-taker',
  templateUrl: './reminder-taker.component.html',
  styleUrls: ['./reminder-taker.component.css']
})
export class ReminderTakerComponent implements OnInit {

  reminder: Reminder;
  reminders: Array<Reminder> = [];
  errMessage: string;
  constructor(private reminderService: ReminderService) { 
    this.reminder = new Reminder();
  }

  ngOnInit() {
  }

  public addReminder() {
    this.reminder.reminderId = (Math.floor(Math.random()*20) + 1).toString();
    if (this.reminder.reminderName === '' || this.reminder.reminderDescription === '') {
      this.errMessage = 'Name and Description both are required fields';
    } else {
      this.reminders.push(this.reminder);
      this.reminderService.createReminder(this.reminder).subscribe(
        () => {
          this.reminder = new Reminder();
        },
        (error) => {
          const index = this.reminders.findIndex(reminder => reminder.reminderName === this.reminder.reminderName);
          this.reminders.splice(index, 1);
          this.errMessage = error.message;
        })
    }
  }

}
