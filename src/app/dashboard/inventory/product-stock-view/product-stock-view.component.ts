 
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductStockView, OrderService } from '../../../services/order.service';
 
@Component({
  selector: 'app-productstockview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-stock-view.component.html',
  styleUrls: ['./product-stock-view.component.css']
})
export class ProductStockViewComponent implements OnInit {
  stockList: ProductStockView[] = [];
  isLoading = true;
  errorMessage: string | null = null;
 
  constructor(private stockService: OrderService) {}
 
  ngOnInit(): void {
    this.loadStock();
  }
 
  loadStock(): void {
    this.isLoading = true;
    this.stockService.getAllProductsStockViews().subscribe({
      next: (data) => {
        // Convert thresholds to numbers and give safe defaults
        this.stockList = data.map(stock => ({
          ...stock,
          minThreshold: Number(stock.minThreshold) || 0,
          maxThreshold: Number(stock.maxThreshold) || Number.MAX_SAFE_INTEGER
        }));
        console.log('Stock data after mapping:', this.stockList);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching stock:', err);
        this.errorMessage = 'Failed to load stock data';
        this.isLoading = false;
      }
    });
  }
}
 
 