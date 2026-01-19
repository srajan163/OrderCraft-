export interface SupplierAddress {
  addressId?: number;
  addressCity: string;
  addressCountry: string;
  addressPostalCode: string;
  addressState: string;
  addressStreet: string;
}

export interface Supplier {
  suppliersId?: number;
  suppliersName: string;
  suppliersPhone: string;
  suppliersEmail: string;
  suppliersContactPerson: string;
  supplierAddress: SupplierAddress;
}
