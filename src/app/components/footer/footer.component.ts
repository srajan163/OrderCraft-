import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Mail, Phone, Globe } from 'lucide-angular';
 
@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  currentYear = new Date().getFullYear();
  environment = {
    version: '2.4.1',
    envName: 'DEV',  // Change dynamically using Angular environments
    lastUpdated: 'Jan 2025',
    serverStatus: 'Online'
  };
 
  icons = { Mail, Phone, Globe };
}