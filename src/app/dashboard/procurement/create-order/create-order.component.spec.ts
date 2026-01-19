import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierOrderCreateComponent } from './create-order.component';

describe('CreateOrderComponent', () => {
  let component: SupplierOrderCreateComponent;
  let fixture: ComponentFixture<SupplierOrderCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierOrderCreateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SupplierOrderCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
