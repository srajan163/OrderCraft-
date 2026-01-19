import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionEfficiencyReportComponent } from './production-efficiency-report.component';

describe('ProductionEfficiencyReportComponent', () => {
  let component: ProductionEfficiencyReportComponent;
  let fixture: ComponentFixture<ProductionEfficiencyReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductionEfficiencyReportComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductionEfficiencyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
