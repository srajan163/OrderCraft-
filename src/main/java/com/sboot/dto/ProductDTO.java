package com.sboot.dto;

public class ProductDTO {
    private Long id;
    private String name;
    private Float price;
    private Integer productsquantity;


    private String category;
    private int  quantity;

    public ProductDTO(Long id, String name, Float price, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

   


    public ProductDTO(Long id, String name, Float price, Integer productsquantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productsquantity = productsquantity;

    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public Float getPrice() { return price; }


    public int getQuantity() { return quantity;}
    public String getCategory() { return category; }
    


    public Integer getProductsQuantity() { return productsquantity;}

}
