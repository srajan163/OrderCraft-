import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionTimelineComponent } from './production-timeline.component';

describe('ProductionTimelineComponent', () => {
  let component: ProductionTimelineComponent;
  let fixture: ComponentFixture<ProductionTimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductionTimelineComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductionTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
