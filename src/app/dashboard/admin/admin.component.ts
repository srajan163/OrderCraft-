import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { UserService, User } from '../../services/user.service';
import { StatsService, OrderSummary, AlertItem } from '../../services/stats.service';
import { Subscription } from 'rxjs';

type ChangeType = 'positive' | 'negative';

interface Stat {
  icon: string;
  label: string;
  value?: string;
  change: string;
  changeType: ChangeType;
  key?: 'users' | 'suppliers' | 'orders' | 'alerts' | string;
}

interface Activity {
  icon: string;
  title: string;
  subtitle: string;
  wrapperBg?: string;
  iconColor?: string;
}

interface QuickAction {
  icon: string;
  title: string;
  subtitle: string;
  cardBg?: string;
  iconColor?: string;
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, OnDestroy {
  // header icons
  headerIcon = 'package';
  bellIcon = 'bell';
  settingsIcon = 'settings';
  logoutIcon = 'log-out';

  // live counts
  userCount: number | null = null;
  supplierCount: number | null = null;

  // orders & alerts
  orderSummary: OrderSummary | null = null;
  alertsList: AlertItem[] = [];

  // activities built from alerts
  recentActivities: Activity[] = [];

  // subscriptions
  private subs = new Subscription();

  // stats tiles
  stats: Stat[] = [
    { icon: 'Users',     label: 'Total Users',      key: 'users',     change: '',    changeType: 'positive' },
    { icon: 'Orders',    label: 'Orders Today',     key: 'orders',    change: '+4',  changeType: 'positive' },
    { icon: 'Suppliers', label: 'Active Suppliers', key: 'suppliers', change: '+1',  changeType: 'positive' },
    { icon: 'Alerts',    label: 'Low Stock Alerts', key: 'alerts',    change: '-2',  changeType: 'negative' },
  ];

  // quick actions
  quickActions: QuickAction[] = [
    { icon: '➕', title: 'Register User', subtitle: 'Add new system user', cardBg: 'card-blue',  iconColor: 'text-blue-600' },
    { icon: '🔓', title: 'Unlock Users',  subtitle: 'Unlock blocked users', cardBg: 'card-green', iconColor: 'text-green-600' },
    { icon: '🔑', title: 'Reset Password', subtitle: 'Reset credentials',   cardBg: 'card-amber', iconColor: 'text-amber-600' }
  ];

  // Notifications
  notifications: string[] = [
    'Order #1023 has been approved',
    'New supplier added successfully',
    'Low stock alert: Product A'
  ];
  showNotifications = false;

  // Users table
  users: User[] = [];
  showAllUsers = false;
  loadingUsers = false;
  loadError: string | null = null;

  constructor(
    private router: Router,
    private userService: UserService,
    private statsService: StatsService
  ) {}

  ngOnInit(): void {
    // client-side guard
    const isAuthenticated = localStorage.getItem('isAuthenticated');
    if (!isAuthenticated) {
      this.router.navigate(['/login']);
      return;
    }

    // start SSE (StatsService will fallback to polling if needed)
    try {
      this.statsService.startSse();
    } catch (e) {
      console.warn('StatsService.startSse failed', e);
    }

    // live stats
    this.subs.add(
      this.statsService.stats$.subscribe({
        next: (s) => {
          if (!s) return;
          this.userCount = s.users;
          this.supplierCount = s.suppliers;
        },
        error: (err) => console.error('stats$ error', err)
      })
    );

    // orders
    this.subs.add(
      this.statsService.orders$.subscribe({
        next: (o: OrderSummary) => { this.orderSummary = o; },
        error: (err) => console.error('orders$ error', err)
      })
    );

    // alerts -> build recent activities
    this.subs.add(
      this.statsService.alerts$.subscribe({
        next: (a: AlertItem[]) => {
          this.alertsList = a || [];
          this.rebuildRecentActivities();
        },
        error: (err) => console.error('alerts$ error', err)
      })
    );

    // One-shots to seed UI fast
    this.statsService.fetchOverview().subscribe({
      next: (s) => { if (s) { this.userCount = s.users; this.supplierCount = s.suppliers; } },
      error: () => {}
    });

    this.statsService.fetchOrdersOnce().subscribe({
      next: (o) => this.orderSummary = o,
      error: () => {}
    });

    this.statsService.fetchAlertsOnce().subscribe({
      next: (a) => { this.alertsList = a || []; this.rebuildRecentActivities(); },
      error: () => {}
    });

    // users table
    this.loadUsers();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
    this.statsService.shutdown();
  }

  // ======= Activities from alerts =======
  private iconForLevel(level: string): string {
    switch ((level || '').toUpperCase()) {
      case 'CRITICAL': return '🚨';
      case 'ERROR':    return '❌';
      case 'WARN':     return '⚠️';
      case 'INFO':     return 'ℹ️';
      default:         return '🔔';
    }
  }

  private colorsForLevel(level: string): { wrapperBg?: string; iconColor?: string } {
    switch ((level || '').toUpperCase()) {
      case 'CRITICAL': return { wrapperBg: 'bg-red-50',   iconColor: 'text-red-700' };
      case 'ERROR':    return { wrapperBg: 'bg-red-50',   iconColor: 'text-red-600' };
      case 'WARN':     return { wrapperBg: 'bg-amber-50', iconColor: 'text-amber-600' };
      case 'INFO':     return { wrapperBg: 'bg-blue-50',  iconColor: 'text-blue-600' };
      default:         return { wrapperBg: 'bg-slate-50', iconColor: 'text-slate-600' };
    }
  }

  private timeAgo(iso?: string): string {
    if (!iso) return '';
    const t = new Date(iso).getTime();
    if (Number.isNaN(t)) return '';
    const diff = Date.now() - t;
    const s = Math.max(1, Math.floor(diff / 1000));
    if (s < 60) return `${s}s ago`;
    const m = Math.floor(s / 60);
    if (m < 60) return `${m}m ago`;
    const h = Math.floor(m / 60);
    if (h < 24) return `${h}h ago`;
    const d = Math.floor(h / 24);
    return `${d}d ago`;
  }

  private rebuildRecentActivities(maxItems = 8): void {
    const alerts = this.alertsList ?? [];
    this.recentActivities = alerts
      .slice()
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      .slice(0, maxItems)
      .map<Activity>(a => {
        const { wrapperBg, iconColor } = this.colorsForLevel(a.level);
        return {
          icon: this.iconForLevel(a.level),
          title: a.message,
          subtitle: `${(a.level || 'INFO').toUpperCase()} • ${a.productName} • Qty ${a.quantity} • ${this.timeAgo(a.createdAt)}`,
          wrapperBg,
          iconColor
        };
      });
  }

  // trackBy to prevent flicker
  trackByActivity = (_: number, a: Activity) => a.title + '|' + a.subtitle;

  // ======= Misc UI helpers =======
  get alertsCountDisplay(): string {
    return String(this.alertsList?.length ?? 0);
  }

  changeColor(type: ChangeType): string {
    return type === 'positive' ? 'text-green-600' : 'text-red-600';
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  // ======= Users table =======
  loadUsers(): void {
    this.loadingUsers = true;
    this.loadError = null;
    this.userService.getAllUsers().subscribe({
      next: (data) => { this.users = data || []; this.loadingUsers = false; },
      error: (err) => { console.error('Failed to load users', err); this.loadError = 'Failed to load users'; this.loadingUsers = false; }
    });
  }

  toggleUsers(): void { this.showAllUsers = !this.showAllUsers; }

  registerUser(): void { this.router.navigate(['/register']); }
  goToProfile() { this.router.navigate(['/profile']); }
  unlockUsers(): void { this.router.navigate(['/unlockuser']); }
  resetPassword(): void { this.router.navigate(['/reset-password']); }

  editUser(user: User): void { this.router.navigate(['/update-user', user.userId]); }

  deleteUser(userId: number | string): void {
    const idStr = String(userId);
    if (!confirm('Are you sure you want to delete this user?')) return;
    this.userService.deleteUser(idStr).subscribe({
      next: () => this.loadUsers(),
      error: (err) => { console.error('Failed to delete user', err); alert('Error deleting user'); }
    });
  }

  logout(): void {
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userRole');
    this.router.navigate(['/']);
  }
}
