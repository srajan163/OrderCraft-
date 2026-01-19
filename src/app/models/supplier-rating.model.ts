export interface SupplierRatingRequest {
  supplierId: number;
  review?: string;
  answers: number[];
}

export interface SupplierRatingResponse {
  supplierId: number;
  supplierName: string;
  rating: number;
  review: string;
  updatedAt: string;
}
