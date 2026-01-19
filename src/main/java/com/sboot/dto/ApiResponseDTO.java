package com.sboot.dto;
 
public class ApiResponseDTO {
    private String message;
    private Long returnOrderId;
 
    public ApiResponseDTO(String message, Long returnOrderId) {
        this.message = message;
        this.returnOrderId = returnOrderId;
    }
 
	public String getMessage() {
		return message;
	}
 
	public void setMessage(String message) {
		this.message = message;
	}
 
	public Long getReturnOrderId() {
		return returnOrderId;
	}
 
	public void setReturnOrderId(Long returnOrderId) {
		this.returnOrderId = returnOrderId;
	}
    
    
 
  
}
 
 
 