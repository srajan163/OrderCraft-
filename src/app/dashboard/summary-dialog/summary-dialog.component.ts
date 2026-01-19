 
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
 
@Component({
  selector: 'app-summary-dialog',
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  templateUrl: './summary-dialog.component.html',
  styleUrls: ['./summary-dialog.component.css']
})
export class SummaryDialogComponent {
 
  constructor(
    public dialogRef: MatDialogRef<SummaryDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}
 
  confirm() {
    this.dialogRef.close('confirm');
  }
}
 