//package com.sboot.entity;
// 
//import jakarta.persistence.*;
// 
//import java.util.Date;
//
//@Entity
// 
//@Table(name = "INVENTORY_TRANSACTION")
// 
//public class InventoryTransaction {
// 
//	@Id
// 
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_transaction_seq")
// 
//	@SequenceGenerator(name = "inventory_transaction_seq", sequenceName = "INVENTORY_TRANSACTION_SEQ", allocationSize = 1)
// 
//	@Column(name = "ITID")
//
//    private Long itId;
//
//    @Column(name = "ITPRODUCTID")
//
//    private Long itProductId;
//
//    @Column(name = "ITPERFORMEDBY")
//
//    private String itPerformedBy;
//    
//    
//
//    @Temporal(TemporalType.TIMESTAMP)
//
//    @Column(name = "ITTRANSACTIONDATE")
//
//    private Date itTransactionDate;
//
//    @Column(name = "ITTRANSACTIONTYPE")
//
//    private String itTransactionType; // e.g. "RETURN", "PURCHASE", "ADJUSTMENT"
//
//    @Column(name = "ITQUANTITY")
//
//    private Integer itQuantity;
//
//    // Getters and Setters
// 
//    public Long getItId() { return itId; }
// 
//    public void setItId(Long itId) { this.itId = itId; }
//
//    public Long getItProductId() { return itProductId; }
// 
//    public void setItProductId(Long itProductId) { this.itProductId = itProductId; }
//
//    public String getItPerformedBy() { return itPerformedBy; }
// 
//    public void setItPerformedBy(String itPerformedBy) { this.itPerformedBy = itPerformedBy; }
//
//    public Date getItTransactionDate() { return itTransactionDate; }
// 
//    public void setItTransactionDate(Date itTransactionDate) { this.itTransactionDate = itTransactionDate; }
//
//    public String getItTransactionType() { return itTransactionType; }
// 
//    public void setItTransactionType(String itTransactionType) { this.itTransactionType = itTransactionType; }
//
//    public Integer getItQuantity() { return itQuantity; }
// 
//    public void setItQuantity(Integer itQuantity) { this.itQuantity = itQuantity; }
// 
//}


package com.sboot.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "INVENTORY_TRANSACTION")
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_transaction_seq")
    @SequenceGenerator(name = "inventory_transaction_seq", sequenceName = "INVENTORY_TRANSACTION_SEQ", allocationSize = 1)
    @Column(name = "ITID")
    private Long itId;

    // Keep the raw FK column if you need it directly
    @Column(name = "ITPRODUCTID", insertable = false, updatable = false)
    private Long itProductId;

    // Relation to Product entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITPRODUCTID", referencedColumnName = "PRODUCTSID")  // assumes Product entity has column PRODUCTS_ID
    private Product product;

    @Column(name = "ITPERFORMEDBY")
    private String itPerformedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ITTRANSACTIONDATE")
    private Date itTransactionDate;

    @Column(name = "ITTRANSACTIONTYPE")
    private String itTransactionType; // e.g. "RETURN", "PURCHASE", "ADJUSTMENT"

    @Column(name = "ITQUANTITY")
    private Integer itQuantity;

    // --- Getters and Setters ---
    public Long getItId() { return itId; }
    public void setItId(Long itId) { this.itId = itId; }

    public Long getItProductId() { return itProductId; }
    public void setItProductId(Long itProductId) { this.itProductId = itProductId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getItPerformedBy() { return itPerformedBy; }
    public void setItPerformedBy(String itPerformedBy) { this.itPerformedBy = itPerformedBy; }

    public Date getItTransactionDate() { return itTransactionDate; }
    public void setItTransactionDate(Date itTransactionDate) { this.itTransactionDate = itTransactionDate; }

    public String getItTransactionType() { return itTransactionType; }
    public void setItTransactionType(String itTransactionType) { this.itTransactionType = itTransactionType; }

    public Integer getItQuantity() { return itQuantity; }
    public void setItQuantity(Integer itQuantity) { this.itQuantity = itQuantity; }
}
