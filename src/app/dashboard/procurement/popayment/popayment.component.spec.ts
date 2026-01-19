import { ComponentFixture, TestBed } from '@angular/core/testing';

import { POPaymentComponent } from './popayment.component';

describe('PopaymentComponent', () => {
  let component: POPaymentComponent;
  let fixture: ComponentFixture<POPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [POPaymentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(POPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
