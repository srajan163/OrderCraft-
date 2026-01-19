package com.sboot.dto;

import lombok.Data;

@Data
public class CustomizableReportingFilterDto {

    private Long id;
    private String name;
    private String description;
    private String criteriaJson;   // JSON string for dynamic criteria
    private Boolean active;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCriteriaJson() {
		return criteriaJson;
	}
	public void setCriteriaJson(String criteriaJson) {
		this.criteriaJson = criteriaJson;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
    
    
}
