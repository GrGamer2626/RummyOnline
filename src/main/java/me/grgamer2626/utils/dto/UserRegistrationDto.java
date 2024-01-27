package me.grgamer2626.utils.dto;

import jakarta.validation.constraints.*;
import me.grgamer2626.utils.validation.FieldMatch;

@SuppressWarnings("unused")
@FieldMatch(first = "password", second = "confirmPassword", message = "Password and confirmation must be identical!")
public class UserRegistrationDto {
	
	@NotEmpty(message = "Nick Name is required")
	@Size(min = 5, max = 30, message = "Nick name must be between 5 and 30 characters")
	@Pattern(regexp = "^[A-Za-z0-9-._]+$", message = "Nick name contains illegal characters.")
	private String nickName;
	
	@NotEmpty(message = "Email is required")
	@Email(message = "Invalid email address")
	private String email;
	
	@NotEmpty(message = "Password is required")
	@Size(min = 8, max = 64, message = "Password must have at least 8 characters")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*_+=|~?-]).*$", message = "The password must contain at least one uppercase letter, lowercase letter, number and a special character.")
	private String password;
	
	@NotEmpty(message = "Password confirmation is required")
	private String confirmPassword;
	
	@AssertTrue(message = "Terms and rules are not accepted!")
	private boolean termsAcceptance;
	
	
	public UserRegistrationDto() {
		this.nickName = "";
		this.email = "";
		this.password = "";
		this.confirmPassword = "";
		this.termsAcceptance = false;
	}
	
	public UserRegistrationDto(String nickName, String email, String password, String confirmPassword, boolean termsAcceptance) {
		this.nickName = nickName;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.termsAcceptance = termsAcceptance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getNickName() {
		return nickName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public boolean isTermsAcceptance() {
		return termsAcceptance;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public void setTermsAcceptance(boolean termsAcceptance) {
		this.termsAcceptance = termsAcceptance;
	}
}
