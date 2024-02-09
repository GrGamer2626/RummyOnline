package me.grgamer2626.utils.dto.forgetPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class ForgetPasswordDto {
	
	@NotEmpty(message = "Email is required")
	@Email(message = "Invalid email address")
	private String email;
	
	public ForgetPasswordDto() {
		this.email = "";
	}
	
	public ForgetPasswordDto(String email) {
		this.email = email;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
