import { Component, ElementRef, AfterViewInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
 
 
/** lucide-angular */
import {
  LucideAngularModule,
  Package,
  TrendingUp,
  Truck,
  Users,
  Shield,
  Clock,
  FileText,
  HelpCircle,
  Phone
} from 'lucide-angular';
 
interface Feature {
  icon: any;            // now holds a lucide icon (not a string)
  title: string;
  description: string;
}
 
interface QuickLink {
  icon: any;            // now holds a lucide icon (not a string)
  text: string;
  href: string;
}
 
@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule, RouterModule, FooterComponent, LucideAngularModule, HeaderComponent],
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements AfterViewInit {
  @ViewChild('videoRef') videoRef!: ElementRef<HTMLVideoElement>;
 
  /** header logo icon */
  // headerIcon = Package;
  header = HeaderComponent.prototype;
 
  /** Feature cards — now using lucide icon refs */
  features: Feature[] = [
    {
      icon: Package,
      title: 'Real-Time Inventory',
      description: 'Monitor stock levels, locations, and movements in real-time across all facilities.'
    },
    {
      icon: TrendingUp,
      title: 'Advanced Analytics',
      description: 'Data-driven insights for demand forecasting and inventory optimization.'
    },
    {
      icon: Truck,
      title: 'Order Tracking',
      description: 'Complete visibility into order lifecycle from placement to delivery.'
    },
    {
      icon: Users,
      title: 'Supplier Management',
      description: 'Streamlined supplier relationships and procurement processes.'
    },
    {
      icon: Shield,
      title: 'Enterprise Security',
      description: 'Role-based access control with comprehensive audit trails.'
    },
    {
      icon: Clock,
      title: 'Automated Workflows',
      description: 'Reduce manual tasks with intelligent automation and alerts.'
    }
  ];
 
  /** Quick links — now using lucide icon refs */
  quickLinks: QuickLink[] = [
    { icon: FileText,   text: 'User Manual',   href: '/manual'  },
    { icon: HelpCircle, text: 'IT Support',    href: '/support' },
    { icon: Phone,      text: 'Contact Admin', href: '/contact' }
  ];
 
  ngAfterViewInit(): void {
    const observer = new IntersectionObserver(
      entries => {
        entries.forEach(entry => {
          if (entry.isIntersecting && this.videoRef?.nativeElement) {
            this.videoRef.nativeElement.play().catch(console.error);
          } else if (this.videoRef?.nativeElement) {
            this.videoRef.nativeElement.pause();
          }
        });
      },
      { threshold: 0.5 }
    );
    if (this.videoRef?.nativeElement) {
      observer.observe(this.videoRef.nativeElement);
    }
  }
}