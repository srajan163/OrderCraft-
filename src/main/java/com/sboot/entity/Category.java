package com.sboot.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "CATEGORIES")
public class Category {

	
    @Id
    @SequenceGenerator(
            name = "category_seq",
            sequenceName = "CATEGORIES_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_seq"
    )
    @Column(name = "CATEGORIESID")
    private Long categoryId;

    @Column(name = "CATEGORYNAME")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionUnit> units;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<ProductionUnit> getUnits() {
		return units;
	}

	public void setUnits(List<ProductionUnit> units) {
		this.units = units;
	}
    

}
