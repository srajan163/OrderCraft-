import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { LucideAngularModule, Home, User, Settings, Package, Factory } from 'lucide-angular';
 
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [LucideAngularModule,CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  // Inputs to customize header content
  @Input() title: string = 'InventoryPro';
  @Input() subtitle: string = 'Enterprise Management System';
  @Input() icon: any = Home;
 
  // For showing role/user info
  @Input() roleName: string | null = null;
 
  // Export icons
  readonly Home = Home;
  readonly User = User;
  readonly Settings = Settings;
  readonly Package = Package;
  readonly Factory = Factory;
}