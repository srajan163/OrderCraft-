// inventory.component.ts
import { Component, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { InactivityService } from '../../services/inactivity.service';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

type StatItem = { label: string; value: string | number; change?: string };
type ActivityItem = { icon: string; title: string; time: string };

@Component({
  selector: 'app-inventory',
  standalone: true,
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css'],
  imports: [RouterModule, CommonModule]
})
export class InventoryComponent implements OnInit, OnDestroy {
  username = 'User';
  showProfileMenu = false;
  showNotifications = false;
  notifications: string[] = [];
  stats: StatItem[] = [];
  recentActivity: ActivityItem[] = [];

  totalProducts = 0;
  lowStock = 0;
  totalSuppliers = 0;
  todayTransactions = 0;

  private docClickUnlistener?: () => void;
  private escListenerUnlistener?: () => void;

  // keep services private; they are used by template methods
  constructor(
    private inactivityService: InactivityService,
    public router: Router,
    private http: HttpClient,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    // username resolution (localStorage or token)
    const lsName = localStorage.getItem('username');
    if (lsName && lsName.trim().length) {
      this.username = lsName;
    } else {
      const token = localStorage.getItem('token');
      const fromToken = this.getUsernameFromToken(token);
      if (fromToken) this.username = fromToken;
    }

    // sample / placeholder data — swap with API calls
    this.notifications = [
      'New purchase order #PO-1023',
      'Low stock: Item A (qty 3)',
      'Supplier X updated lead time'
    ];

    this.http.get<any>('http://localhost:8086/api/kpi')
    .subscribe(res => {
      this.stats = [
        res.totalProducts,
        // enable when backend is ready:
        res.lowStock,
        res.suppliers,
        // res.todayTransactions
      ];
      this.syncSummaryFromStats();
    });

    // this.stats = [
    //   { label: 'Total Products', value: 1248, change: '+2.3%' },
    //   { label: 'Low Stock', value: 18, change: '+6%' },
    //   { label: 'Suppliers', value: 42, change: '—' },
    //   { label: 'Today Transactions', value: 56, change: '-1.2%' }
    // ];

    this.recentActivity = [
      { icon: '📦', title: 'Received goods — Item B (50 units)', time: '10m ago' },
      { icon: '⚠️', title: 'Low stock alert — Item A', time: '35m ago' },
      { icon: '📤', title: 'Dispatched order #SO-899', time: '2h ago' }
    ];

    // populate summary numbers from stats for quick binding
    this.syncSummaryFromStats();

    // global click listener to close popovers when clicking outside
    this.docClickUnlistener = this.renderer.listen('document', 'click', (ev: Event) => {
      // only close if click happened outside known toggles; handlers already stopPropagation where needed
      if (this.showProfileMenu) this.showProfileMenu = false;
      if (this.showNotifications) this.showNotifications = false;
    });

    // close menus with ESC
    this.escListenerUnlistener = this.renderer.listen('document', 'keydown', (ev: KeyboardEvent) => {
      if (ev.key === 'Escape') {
        this.showProfileMenu = false;
        this.showNotifications = false;
      }
    });
  }

  ngOnDestroy(): void {
    this.docClickUnlistener?.();
    this.escListenerUnlistener?.();
  }

  private resolveUsername(): void {
    const lsName = localStorage.getItem('username')?.trim();
    if (lsName) {
      this.username = lsName;
      return;
    }

    const token = localStorage.getItem('token');
    const fromToken = this.getUsernameFromToken(token);
    if (fromToken) this.username = fromToken;
  }

  getUsernameFromToken(token: string | null): string | null {
    if (!token) return null;
    try {
      const parts = token.split('.');
      if (parts.length < 2) return null;
      const payloadBase64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      const payloadJson = decodeURIComponent(
        atob(payloadBase64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      const payload = JSON.parse(payloadJson);
      return (payload.username || payload.sub || payload.name) ?? null;
    } catch {
      return null;
    }
  }

  toggleProfileMenu(event?: Event): void {
    event?.stopPropagation();
    this.showProfileMenu = !this.showProfileMenu;
    if (this.showProfileMenu) this.showNotifications = false;
  }

  toggleNotifications(event?: Event): void {
    event?.stopPropagation();
    this.showNotifications = !this.showNotifications;
    if (this.showNotifications) this.showProfileMenu = false;
  }

  viewProfile(): void {
    this.showProfileMenu = false;
    this.router.navigate(['/view-profile']);
  }

  goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  updatePassword(): void {
    this.showProfileMenu = false;
    this.router.navigate(['/reset-password']);
  }

  logout(): void {
    this.inactivityService.logout();
  }

  // Navigation shortcuts
  productStock(): void {
    this.router.navigate(['/inventory/product-stock-view']);
  }

  productionTracking(): void {
    this.router.navigate(['/inventory/production-tracking']);
  }

  product(): void {
    this.router.navigate(['/inventory/product']);
  }

  addCategory(): void {
    this.router.navigate(['/inventory/categories']);
  }

  productionschedule(): void {
    this.router.navigate(['/inventory/production-schedule']);
  }

  report(): void {
    this.router.navigate(['/inventory/report']);
  }

  // small helpers
  private initPlaceholderData(): void {
    // replace these calls with real API responses as you wire them up
    this.notifications = [
      'New purchase order #PO-1023',
      'Low stock alert: Widget 42 (qty 3)',
      'Supplier "Acme Co." updated lead time'
    ];
    this.stats = [
      { label: 'Total Products', value: 1248, change: '+2.3%' },
      { label: 'Low Stock', value: 18, change: '+6%' },
      { label: 'Suppliers', value: 42 },
      { label: 'Today Transactions', value: 56, change: '-1.2%' }
    ];
    this.recentActivity = [
      { icon: 'box', title: 'Received goods — Item B (50 units)', time: '10 minutes ago' },
      { icon: 'alert', title: 'Low stock alert — Item A', time: '35 minutes ago' },
      { icon: 'truck', title: 'Dispatched order #SO-899', time: '2 hours ago' }
    ];
  }

  private syncSummaryFromStats(): void {
    this.totalProducts = Number(this.stats[0]?.value) || 0;
    this.lowStock = Number(this.stats[1]?.value) || 0;
    this.totalSuppliers = Number(this.stats[2]?.value) || 0;
    this.todayTransactions = Number(this.stats[3]?.value) || 0;
  }
}
