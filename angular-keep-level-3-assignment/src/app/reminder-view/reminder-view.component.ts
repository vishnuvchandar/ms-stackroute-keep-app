import { Component, OnInit, Input } from '@angular/core';
import { ReminderService } from '../services/reminder.service';
import { Reminder } from '../reminder';

@Component({
  selector: 'app-reminder-view',
  templateUrl: './reminder-view.component.html',
  styleUrls: ['./reminder-view.component.css']
})
export class ReminderViewComponent implements OnInit {

  @Input() reminder: Reminder;
  reminders: Array<Reminder> = [];
  errMessage: string;
  reminderObj: Reminder;

  
  constructor(private reminderService: ReminderService) { 
    this.reminder = new Reminder();
   }

  ngOnInit() {
    this.reminderService.getreminders().subscribe((data) => {
      this.reminders = data;
    }, (err) => {
      if(err.status != 404) {
        this.errMessage = err.message;
      }
      this.reminders = [];
    });
  }


  deleteReminder() {
    this.reminderService.deleteReminder(this.reminder.reminderId).subscribe(res => {
      this.reminder = new Reminder();
    })
  }

}
