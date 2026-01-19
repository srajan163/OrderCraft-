import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionScheduleComponent } from './production-schedule.component';

describe('ProductionSchedulesComponent', () => {
  let component: ProductionScheduleComponent;
  let fixture: ComponentFixture<ProductionScheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductionScheduleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductionScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
