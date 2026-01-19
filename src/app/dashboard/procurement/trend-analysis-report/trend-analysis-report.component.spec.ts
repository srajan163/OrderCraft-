import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrendAnalysisReportComponent } from './trend-analysis-report.component';

describe('TrendAnalysisReportComponent', () => {
  let component: TrendAnalysisReportComponent;
  let fixture: ComponentFixture<TrendAnalysisReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrendAnalysisReportComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TrendAnalysisReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
