export class Reminder {
    reminderId: string;
    reminderName: string;
    reminderDescription: string;
    reminderType: string;
    reminderCreatedBy: string;

    constructor() {
        this.reminderCreatedBy = '';
        this.reminderDescription = '';
        this.reminderName = '';
        this.reminderType = '';
    }
}