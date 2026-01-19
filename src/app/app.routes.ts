import { Routes } from '@angular/router';

import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { FooterComponent } from './components/footer/footer.component';
import { AdminComponent } from './dashboard/admin/admin.component';
import { ProcurementComponent } from './dashboard/procurement/procurement.component';
import { ProductionComponent } from './dashboard/production/production.component';
import { InventoryComponent } from './dashboard/inventory/inventory.component';
import { ResetPasswordComponent } from './password/reset-password/reset-password.component';
import { ForgotPasswordComponent } from './password/forgotpassword/forgotpassword.component';
import { RegisterComponent } from './dashboard/register/register.component';
import { UnlockUserComponent } from './dashboard/unlock-user/unlock-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { OrderHistoryComponent } from './dashboard/procurement/order-history/order-history.component';
import { TrackOrderComponent } from './dashboard/procurement/track-order/track-order.component';
import { ReturnOrderComponent } from './dashboard/procurement/return-order/return-order.component';
import { authGuard } from './auth.guard';
import { PaymentComponent } from './payment/payment.component';
import { OrderCancelComponent } from './dashboard/procurement/cancel-order/cancel-order.component';
import { UpdateOrderComponent } from './dashboard/procurement/update-order/update-order.component';
import { SupplierOrderCreateComponent } from './dashboard/procurement/create-order/create-order.component';
import { CustomerOrderComponent } from './dashboard/procurement/customer-order/customer-order.component';
import { ProductStockViewComponent } from './dashboard/inventory/product-stock-view/product-stock-view.component';
import { ProductionTrackingComponent } from './dashboard/inventory/production-tracking/production-tracking.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ProductComponent } from './dashboard/inventory/product/product.component';
import { ReportComponent } from './dashboard/inventory/report/report.component';
import { POPaymentComponent } from './dashboard/procurement/popayment/popayment.component';
import { HomeComponent } from './dashboard/procurement/home/home.component';
import { ProductionTimelineComponent } from './dashboard/production/production-timeline/production-timeline.component';

import { ProductionTimelineDetailComponent } from './dashboard/production/production-timeline-detail/production-timeline-detail.component';
import { ProductionScheduleComponent } from './dashboard/production/production-schedule/production-schedule.component';
import { ProductionAnalyticsComponent } from './dashboard/production/production-analytics/production-analytics.component';
import { SPaymentComponent } from './dashboard/procurement/payment/payment.component';

import { SupplierManagementComponent } from './dashboard/supplier-management/supplier-management.component';
import { SupplierHistoryComponent } from './dashboard/procurement/supplier-history/supplier-history.component';

import { ProductionEfficiencyReportComponent } from './dashboard/production/production-efficiency-report/production-efficiency-report.component';
import { SupplierPerformanceComponent } from './dashboard/procurement/supplier-performance/supplier-performance.component';
import { CategoryComponent } from './dashboard/inventory/category/category.component';
import { TrendAnalysisComponent } from './dashboard/procurement/trend-analysis-report/trend-analysis-report.component';




 
// ✅ Correct path: use production dashboard schedule component
 
export const routes: Routes = [
  // ✅ Default Landing & Login
  { path: '', component: LandingPageComponent, pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'footer', component: FooterComponent },

  // ✅ Admin
  { path: 'admin', component: AdminComponent, canActivate: [authGuard] },

  // ✅ Procurement
  {
    path: 'procurement',
    component: ProcurementComponent,
    canActivate: [authGuard],
    children: [
      { path: 'orders', component: OrderHistoryComponent },
      { path: 'track-order/:id', component: TrackOrderComponent },
      { path: 'track-order', component: TrackOrderComponent },
      { path: 'return-order', component: ReturnOrderComponent },
      { path: 'home', component: HomeComponent },
      { path: 'create-order', component: SupplierOrderCreateComponent },
      { path: 'update-order', component: UpdateOrderComponent },
      { path: 'cancel-order', component: OrderCancelComponent },
      { path: 'customer-order', component: CustomerOrderComponent },
      { path: 'popayment', component: POPaymentComponent },
      {path:"payment",component:SPaymentComponent},
      { path: "supplier-performance", component: SupplierPerformanceComponent},
      { path: 'supplier-management', component: SupplierManagementComponent },
      {path: 'trend-report',component: TrendAnalysisComponent},
      { path: 'supplier-history', component: SupplierHistoryComponent },


      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  },

  // ✅ Production.
  {
    path: 'production',
    component: ProductionComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'timeline', pathMatch: 'full' },
      { path: 'timeline', component: ProductionTimelineComponent },
      { path: 'timeline/:psId', component: ProductionTimelineDetailComponent },
      { path: 'analytics', component: ProductionAnalyticsComponent },
      {path: 'production-efficiency-report',component: ProductionEfficiencyReportComponent}
    
      
    ]
  },
  { path: 'production-schedule', component: ProductionScheduleComponent, canActivate: [authGuard] },


  {
    path: 'inventory',
    component: InventoryComponent,
    canActivate: [authGuard],
    children: [
      { path: 'product-stock-view', component: ProductStockViewComponent },
      { path: 'production-tracking', component: ProductionTrackingComponent },
      { path: 'product', component: ProductComponent },
      { path: 'report', component: ReportComponent },
      { path: 'categories', component: CategoryComponent },



      { path: '', redirectTo: 'product-stock-view', pathMatch: 'full' }
    ]
  },

  // ✅ Supplier Management
  {
    path: 'supplier-management',
    canActivate: [authGuard],
    children: [
      // { path: 'supplier-list', component: SupplierListComponent },
      // { path: 'supplier-add', component: SupplierAddComponent },
      // { path: 'supplier-edit/:id', component: SupplierEditComponent },
     
      { path: '', redirectTo: 'list', pathMatch: 'full' }
    ]
  },

  // ✅ Payments
  { path: 'payments/pay', component: PaymentComponent },

  // ✅ Auth & User Management
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'reset-password', component: ResetPasswordComponent, canActivate: [authGuard] },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'register', component: RegisterComponent, canActivate: [authGuard] },
  { path: 'update-user/:id', component: UpdateUserComponent, canActivate: [authGuard] },
  { path: 'unlockuser', component: UnlockUserComponent, canActivate: [authGuard] },

  // ✅ Wildcard fallback (always last)
  { path: '**', redirectTo: '' }
];
