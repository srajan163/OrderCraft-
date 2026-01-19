import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router'; // ✅ Import RouterOutlet
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';


@Component({
  selector: 'app-root',
  standalone: true,

  imports: [CommonModule, RouterOutlet, FormsModule], // ✅ Add RouterOutlet here

  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})



export class AppComponent {
  title="Squad 4"
  currentYear = new Date().getFullYear(); // ✅ Fix for the 2nd error
}
