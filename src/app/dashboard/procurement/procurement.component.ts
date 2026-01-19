// procurement.component.ts
import { Component, AfterViewInit, ElementRef, ViewChild, HostListener } from '@angular/core';
import { Router, RouterModule, RouterOutlet, ActivatedRoute } from '@angular/router';
import { InactivityService } from '../../services/inactivity.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-procurement-dashboard',
  standalone: true,
  templateUrl: './procurement.component.html',
  styleUrls: ['./procurement.component.css'],
  imports: [
    CommonModule,
    RouterModule,
    RouterOutlet,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule
  ],
})
export class ProcurementComponent implements AfterViewInit {
  @ViewChild('headerRef', { read: ElementRef }) headerRef?: ElementRef<HTMLElement>;

  menuOpen = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private inactivityService: InactivityService
  ) {}

  /* ---- lifecycle ---- */
  ngAfterViewInit(): void {
    // set CSS var for top offset so child pages can size themselves:
    // make sure your header element in template has #headerRef
    setTimeout(() => {
      try {
        const headerEl = this.headerRef?.nativeElement;
        if (headerEl) {
          const rect = headerEl.getBoundingClientRect();
          // include some vertical gap/padding to match layout (adjust +20 if you need more)
          const topOffset = Math.round(rect.height + 20);
          document.documentElement.style.setProperty('--top-offset', `${topOffset}px`);
        } else {
          // default fallback
          document.documentElement.style.setProperty('--top-offset', `130px`);
        }
      } catch (e) {
        document.documentElement.style.setProperty('--top-offset', `130px`);
      }
    }, 0);
  }

  /* ---- menu / UI ---- */
  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  // close menu when clicking outside
  @HostListener('document:click', ['$event'])
  onDocumentClick(evt: MouseEvent) {
    const target = evt.target as HTMLElement;
    // if clicked outside user-info button or dropdown, close menu
    if (!target.closest('.user-info')) {
      this.menuOpen = false;
    }
  }

  // close menu on ESC
  @HostListener('document:keydown.escape', ['$event'])
  onEscapeKey() {
    this.menuOpen = false;
  }

  /* ---- navigation helpers (use relative routes) ---- */
  trackOrder(orderId?: number): void {
    if (orderId != null) {
      this.router.navigate(['track-order', orderId], { relativeTo: this.route });
    } else {
      this.router.navigate(['track-order'], { relativeTo: this.route });
    }
  }

  createOrder(): void {
    this.router.navigate(['create-order'], { relativeTo: this.route });
  }

  updateOrder(orderId?: number): void {
    if (orderId != null) {
      this.router.navigate(['update-order', orderId], { relativeTo: this.route });
    } else {
      this.router.navigate(['update-order'], { relativeTo: this.route });
    }
  }

  cancelOrder(orderId?: number): void {
    if (orderId != null) {
      this.router.navigate(['cancel-order', orderId], { relativeTo: this.route });
    } else {
      this.router.navigate(['cancel-order'], { relativeTo: this.route });
    }
  }

  customerOrder(): void {
    this.router.navigate(['customer-order'], { relativeTo: this.route });
  }

  returnOrder(orderId?: number): void {
    if (orderId != null) {
      this.router.navigate(['return-order', orderId], { relativeTo: this.route });
    } else {
      this.router.navigate(['return-order'], { relativeTo: this.route });
    }
  }

  goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  updatepassword(): void {
    this.router.navigate(['/reset-password']);
  }

  logout(): void {
    this.inactivityService.logout();
  }

  // navigate to payment child route (relative)
  Payment(): void {
    this.router.navigate(['payment'], { relativeTo: this.route });
  }

  //changes

  SupplierManagement(): void {
    this.router.navigate(['supplier-management'], { relativeTo: this.route });
  }

  trendReport(): void {
  this.router.navigate(['trend-report'], { relativeTo: this.route });
  }

  supplierHistory(): void {
    this.router.navigate(['supplier-history'], { relativeTo: this.route });
  }

  supplierPerformanceReport(): void {
    this.router.navigate(['supplier-performance'], { relativeTo: this.route })
  }




}
