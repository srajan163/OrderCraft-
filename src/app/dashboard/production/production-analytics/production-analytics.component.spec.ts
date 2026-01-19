import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionAnalyticsComponent } from './production-analytics.component';

describe('ProductionAnalyticsComponent', () => {
  let component: ProductionAnalyticsComponent;
  let fixture: ComponentFixture<ProductionAnalyticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductionAnalyticsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductionAnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
