package com.sboot.dto;

import com.sboot.entity.Role;
import com.sboot.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String userName;
    private String userEmail;
    private boolean accountLocked;
    private String userFullName;
    private Role role;

    

	public static UserDto fromEntity(User user) {
        return new UserDto(
            user.getUserId(),
            user.getUserName(),
            user.getUserEmail(),
            user.getAccountLockedAt() != null,
            user.getUserFullName(),
            user.getRole()
        );
    }



	public String getUserFullName() {
		return userFullName;
	}



	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}



	public UserDto(String userId, String userName, String userEmail, boolean accountLocked, String userFullName, Role role) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.accountLocked = accountLocked;
		this.userFullName = userFullName;
		this.role = role;
	}



	public Role getRole() {
		return role;
	}



	public void setRole(Role role) {
		this.role = role;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserEmail() {
		return userEmail;
	}



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	public boolean isAccountLocked() {
		return accountLocked;
	}



	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	
	
	
}
