import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { EditNoteViewComponent } from '../edit-note-view/edit-note-view.component';
import { ActivatedRoute } from '@angular/router';
import { RouterService } from '../services/router.service';
@Component({
  selector: 'app-edit-note-opener',
  templateUrl: './edit-note-opener.component.html',
  styleUrls: ['./edit-note-opener.component.css']
})
export class EditNoteOpenerComponent {

  constructor( private matDialogue: MatDialog, private route: ActivatedRoute, private routeServ: RouterService) {
    const noteId = this.route.snapshot.paramMap.get('noteId');
    this.matDialogue.open(EditNoteViewComponent,
      {
        data: { noteId: `${noteId}`}
      })
      .afterClosed().subscribe(() => {
        this.routeServ.routeBack();
      });
  }



}
