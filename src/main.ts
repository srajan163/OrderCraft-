import { HTTP_INTERCEPTORS, provideHttpClient } from '@angular/common/http';
import { AuthInterceptor } from './app/interceptors/auth.interceptor';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { importProvidersFrom } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { Chart } from 'chart.js';
import { registerables } from 'chart.js';
Chart.register(...registerables);

import { LucideAngularModule, Home, Menu } from 'lucide-angular';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
 
bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
     importProvidersFrom(BrowserAnimationsModule, LucideAngularModule.pick({ Home, Menu })), // <-- ✅ ADD THIS LINE

    provideRouter(routes),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,

    }, provideAnimationsAsync()
  ]
})
.catch(err => console.error(err));
 
 
 

