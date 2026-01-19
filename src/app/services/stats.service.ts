// src/app/services/stats.service.ts
import { Injectable, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subscription, timer } from 'rxjs';
import { switchMap } from 'rxjs/operators';

/* -------------------------
   Shared DTOs (exported)
--------------------------*/
export interface StatsOverview {
  users: number;
  suppliers: number;
}

export interface OrderSummary {
  totalOrders: number;
  pending: number;
  shipped: number;
  delivered: number;
}

export interface AlertItem {
  level: 'INFO' | 'WARN' | 'ERROR' | 'CRITICAL' | string;
  message: string;
  productId: number;
  productName: string;
  quantity: number;
  createdAt: string; // ISO
}


/* -------------------------
   StatsService
   - Supports three SSE endpoints:
     /api/stats/stream   -> StatsOverview (named event 'stats' or 'message')
     /api/orders/stream  -> OrderSummary (named event 'orders' or 'message')
     /api/alerts/stream  -> AlertItem[] (named event 'alerts' or 'message')
   - Falls back to polling for each if SSE fails.
--------------------------*/
@Injectable({ providedIn: 'root' })
export class StatsService {
  private apiBase = 'http://localhost:8086/api'; // base for all endpoints

  // Subjects
  private _stats$ = new BehaviorSubject<StatsOverview>({ users: 0, suppliers: 0 });
  private _orders$ = new BehaviorSubject<OrderSummary>({
    totalOrders: 0,
    pending: 0,
    shipped: 0,
    delivered: 0
  });
  private _alerts$ = new BehaviorSubject<AlertItem[]>([]);

  // Observables (public)
  readonly stats$ = this._stats$.asObservable();
  readonly orders$ = this._orders$.asObservable();
  readonly alerts$ = this._alerts$.asObservable();

  // EventSource instances (one per stream)
  private statsEs: EventSource | null = null;
  private ordersEs: EventSource | null = null;
  private alertsEs: EventSource | null = null;

  // Polling subscriptions (fallback)
  private statsPollingSub?: Subscription;
  private ordersPollingSub?: Subscription;
  private alertsPollingSub?: Subscription;

  constructor(private http: HttpClient, private zone: NgZone) {}

  /* --------------------
     One-shot REST fetches
     -------------------- */
  fetchOverview(): Observable<StatsOverview> {
    return this.http.get<StatsOverview>(`${this.apiBase}/stats/overview`);
  }

  fetchOrdersOnce(): Observable<OrderSummary> {
    return this.http.get<OrderSummary>(`${this.apiBase}/orders/overview`); // or /orders ; adjust to your API
  }

  fetchAlertsOnce(): Observable<AlertItem[]> {
    return this.http.get<AlertItem[]>(`${this.apiBase}/alerts/overview`); // or /alerts ; adjust to your API
  }

  /* --------------------
     Start all SSE streams.
     Safe to call multiple times (idempotent).
     -------------------- */
  startSse(): void {
    this.startStatsSse();
    this.startOrdersSse();
    this.startAlertsSse();
  }

  private startStatsSse(): void {
    if (this.statsEs) return;

    try {
      const url = `${this.apiBase}/stats/stream`;
      this.statsEs = new EventSource(url);

      // named event handler (if server sends named event)
      this.statsEs.addEventListener('stats', (ev: Event) => {
        this.handleStatsMessage(ev as MessageEvent);
      });

      // fallback "message"
      this.statsEs.onmessage = (ev: MessageEvent) => {
        this.handleStatsMessage(ev);
      };

      this.statsEs.onopen = () => console.log('Stats SSE connected:', url);

      this.statsEs.onerror = (err) => {
        console.warn('Stats SSE error:', err);
        this.stopStatsSse();
        this.startStatsPollingFallback();
      };
    } catch (err) {
      console.error('Failed to start stats SSE', err);
      this.startStatsPollingFallback();
    }
  }

  private handleStatsMessage(ev: MessageEvent): void {
    this.zone.run(() => {
      try {
        const parsed = JSON.parse(ev.data) as StatsOverview;
        if (parsed && typeof parsed.users === 'number' && typeof parsed.suppliers === 'number') {
          this._stats$.next(parsed);
        } else {
          // ignore unexpected payload
        }
      } catch (e) {
        console.error('Invalid stats SSE payload', e);
      }
    });
  }

  private stopStatsSse(): void {
    if (this.statsEs) {
      try { this.statsEs.close(); } catch (_) { /* ignore */ }
      this.statsEs = null;
    }
  }

  private startStatsPollingFallback(intervalMs = 5000): void {
    if (this.statsPollingSub) return;
    this.statsPollingSub = timer(0, intervalMs)
      .pipe(switchMap(() => this.fetchOverview()))
      .subscribe({
        next: s => this._stats$.next(s),
        error: e => console.error('Polling stats error', e)
      });
  }

  private stopStatsPolling(): void {
    this.statsPollingSub?.unsubscribe();
    this.statsPollingSub = undefined;
  }

  /* --------------------
     Orders SSE (separate endpoint)
     -------------------- */
  private startOrdersSse(): void {
    if (this.ordersEs) return;

    try {
      const url = `${this.apiBase}/orders/stream`;
      this.ordersEs = new EventSource(url);

      this.ordersEs.addEventListener('orders', (ev: Event) => {
        this.handleOrdersMessage(ev as MessageEvent);
      });

      this.ordersEs.onmessage = (ev: MessageEvent) => {
        this.handleOrdersMessage(ev);
      };

      this.ordersEs.onopen = () => console.log('Orders SSE connected:', url);

      this.ordersEs.onerror = (err) => {
        console.warn('Orders SSE error:', err);
        this.stopOrdersSse();
        this.startOrdersPollingFallback();
      };
    } catch (err) {
      console.error('Failed to start orders SSE', err);
      this.startOrdersPollingFallback();
    }
  }

  private handleOrdersMessage(ev: MessageEvent): void {
    this.zone.run(() => {
      try {
        const parsed = JSON.parse(ev.data) as OrderSummary;
        if (parsed && typeof parsed.totalOrders === 'number') {
          this._orders$.next(parsed);
        }
      } catch (e) {
        console.error('Invalid orders SSE payload', e);
      }
    });
  }

  private stopOrdersSse(): void {
    if (this.ordersEs) {
      try { this.ordersEs.close(); } catch (_) { /* ignore */ }
      this.ordersEs = null;
    }
  }

  private startOrdersPollingFallback(intervalMs = 5000): void {
    if (this.ordersPollingSub) return;
    this.ordersPollingSub = timer(0, intervalMs)
      .pipe(switchMap(() => this.fetchOrdersOnce()))
      .subscribe({
        next: o => this._orders$.next(o),
        error: e => console.error('Polling orders error', e)
      });
  }

  private stopOrdersPolling(): void {
    this.ordersPollingSub?.unsubscribe();
    this.ordersPollingSub = undefined;
  }

  /* --------------------
     Alerts SSE (separate endpoint)
     -------------------- */
  private startAlertsSse(): void {
    if (this.alertsEs) return;

    try {
      const url = `${this.apiBase}/alerts/stream`;
      this.alertsEs = new EventSource(url);

      this.alertsEs.addEventListener('alerts', (ev: Event) => {
        this.handleAlertsMessage(ev as MessageEvent);
      });

      this.alertsEs.onmessage = (ev: MessageEvent) => {
        this.handleAlertsMessage(ev);
      };

      this.alertsEs.onopen = () => console.log('Alerts SSE connected:', url);

      this.alertsEs.onerror = (err) => {
        console.warn('Alerts SSE error:', err);
        this.stopAlertsSse();
        this.startAlertsPollingFallback();
      };
    } catch (err) {
      console.error('Failed to start alerts SSE', err);
      this.startAlertsPollingFallback();
    }
  }

  private handleAlertsMessage(ev: MessageEvent): void {
    this.zone.run(() => {
      try {
        const parsed = JSON.parse(ev.data) as AlertItem[] | AlertItem;
        // Accept both a single alert or an array
        if (Array.isArray(parsed)) {
          this._alerts$.next(parsed);
        } else if (parsed && typeof (parsed as AlertItem).message === 'string') {
          // push single alert to the existing list
          const cur = [...this._alerts$.getValue()];
          cur.unshift(parsed as AlertItem);
          this._alerts$.next(cur);
        }
      } catch (e) {
        console.error('Invalid alerts SSE payload', e);
      }
    });
  }

  private stopAlertsSse(): void {
    if (this.alertsEs) {
      try { this.alertsEs.close(); } catch (_) { /* ignore */ }
      this.alertsEs = null;
    }
  }

  private startAlertsPollingFallback(intervalMs = 5000): void {
    if (this.alertsPollingSub) return;
    this.alertsPollingSub = timer(0, intervalMs)
      .pipe(switchMap(() => this.fetchAlertsOnce()))
      .subscribe({
        next: a => this._alerts$.next(a),
        error: e => console.error('Polling alerts error', e)
      });
  }

  private stopAlertsPolling(): void {
    this.alertsPollingSub?.unsubscribe();
    this.alertsPollingSub = undefined;
  }

  /* --------------------
     Stop everything
     -------------------- */
  stopSse(): void {
    this.stopStatsSse();
    this.stopOrdersSse();
    this.stopAlertsSse();
  }

  stopPolling(): void {
    this.stopStatsPolling();
    this.stopOrdersPolling();
    this.stopAlertsPolling();
  }

  shutdown(): void {
    this.stopSse();
    this.stopPolling();
  }
}
