import { Category } from "./Category";
import { Reminder } from "./reminder";

export class Note {
  id: Number;
  title: string;
  text: string;
  state: string;
  noteCreatedBy: string;
  category: Category;
  reminders: Array<Reminder>;

  constructor() {
    this.title = '';
    this.text = '';
    this.state = 'not-started';
    this.noteCreatedBy = '';
    this.category = new Category();
    this.reminders = [];
  }
}
