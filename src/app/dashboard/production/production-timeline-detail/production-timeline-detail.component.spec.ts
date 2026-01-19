import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionTimelineDetailComponent } from './production-timeline-detail.component';

describe('ProductionTimelineDetailComponent', () => {
  let component: ProductionTimelineDetailComponent;
  let fixture: ComponentFixture<ProductionTimelineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductionTimelineDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductionTimelineDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
