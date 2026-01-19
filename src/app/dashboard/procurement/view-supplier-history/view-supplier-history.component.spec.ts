import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSupplierHistoryComponent } from './view-supplier-history.component';

describe('ViewSupplierHistoryComponent', () => {
  let component: ViewSupplierHistoryComponent;
  let fixture: ComponentFixture<ViewSupplierHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewSupplierHistoryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewSupplierHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
