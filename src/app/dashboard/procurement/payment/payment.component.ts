import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SupplierPayment, SupplierPaymentService } from '../../../services/supplier-payment.service';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class SPaymentComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // Create form
  createForm = this.fb.group({
    suppliersId: [null, [Validators.required]],
    suppliersName: [''], // optional — backend uses only id
    invoiceNumber: ['', [Validators.required]],
    invoiceDate: ['', [Validators.required]],
    dueDate: ['', [Validators.required]],
    amount: [null, [Validators.required, Validators.min(0.01)]]
  });

  creating = false;
  createError = '';

  // List + actions
  payments: SupplierPayment[] = [];
  loading = false;
  loadError = '';
  payAmount: Record<number, number> = {};  // inline amount keyed by paymentId

  // Simple filters
  supplierFilter: number | null = null;
  onlyOutstanding = false;

  constructor(
    private fb: FormBuilder,
    private api: SupplierPaymentService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ---- Create ----
  submitCreate(): void {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    this.creating = true;
    this.createError = '';

    const v = this.createForm.value;
    const payload: SupplierPayment = {
      supplier: { suppliersId: Number(v.suppliersId), suppliersName: v.suppliersName || undefined },
      invoiceNumber: v.invoiceNumber!,
      invoiceDate: v.invoiceDate!, // yyyy-MM-dd
      dueDate: v.dueDate!,
      amount: Number(v.amount)
    };

    this.api.createPayment(payload)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.creating = false))
      )
      .subscribe({
        next: () => {
          this.createForm.reset();
          // clear validators state (optional)
          this.createForm.markAsPristine();
          this.createForm.markAsUntouched();
          this.load(); // refresh list
        },
        error: (err) => {
          // prefer structured error message if backend returned one
          this.createError = err?.error?.error || err?.message || 'Create failed';
        }
      });
  }

  // ---- List / Filters ----
  load(): void {
    this.loading = true;
    this.loadError = '';

    const req$ = this.supplierFilter
      ? this.api.getBySupplier(this.supplierFilter)
      : (this.onlyOutstanding ? this.api.getOutstanding() : this.api.getAll());

    req$
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.loading = false))
      )
      .subscribe({
        next: (res) => {
          this.payments = Array.isArray(res) ? res : [];
        },
        error: (err) => {
          this.loadError = err?.error?.error || err?.message || 'Failed to load';
        }
      });
  }

  resetFilters(): void {
    this.supplierFilter = null;
    this.onlyOutstanding = false;
    this.load();
  }

  // ---- Actions ----
  pay(p: SupplierPayment): void {
    const raw = this.payAmount[p.paymentId!];
    const amt = Number(raw);
    if (!Number.isFinite(amt) || amt <= 0) {
      alert('Enter a positive amount');
      return;
    }

    // Disable further pay actions for this payment while processing
    const original = this.payAmount[p.paymentId!];
    this.payAmount[p.paymentId!] = NaN; // temporary locking UI signal (template can check isFinite)

    this.api.applyPayment(p.paymentId!, amt)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (updated) => {
          const idx = this.payments.findIndex(x => x.paymentId === updated.paymentId);
          if (idx > -1) this.payments[idx] = updated;
          this.payAmount[p.paymentId!] = 0;
        },
        error: (err) => {
          this.payAmount[p.paymentId!] = original;
          alert(err?.error?.error || err?.message || 'Payment failed');
        }
      });
  }

  remove(p: SupplierPayment): void {
    if (!confirm(`Delete payment ${p.invoiceNumber}?`)) return;

    // optimistic UI: keep a copy in case of failure
    const before = [...this.payments];
    this.payments = this.payments.filter(x => x.paymentId !== p.paymentId);

    this.api.delete(p.paymentId!)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          // deleted successfully; nothing else to do
        },
        error: (err) => {
          // revert on error
          this.payments = before;
          alert(err?.error?.error || err?.message || 'Delete failed');
        }
      });
  }
}
