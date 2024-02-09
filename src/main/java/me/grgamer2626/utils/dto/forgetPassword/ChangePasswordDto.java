package me.grgamer2626.utils.dto.forgetPassword;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.grgamer2626.utils.validation.FieldMatch;

@FieldMatch(first = "password", second = "confirmPassword", message = "Password and confirmation must be identical!")
public class ChangePasswordDto {
	
	@NotEmpty(message = "Password is required")
	@Size(min = 8, max = 64, message = "Password must have at least 8 characters")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*_+=|~?-]).*$", message = "The password must contain at least one uppercase letter, lowercase letter, number and a special character.")
	private String password;
	
	@NotEmpty(message = "Password confirmation is required")
	private String confirmPassword;
	
	public ChangePasswordDto() {
		this.password = "";
		this.confirmPassword = "";
	}
	
	public ChangePasswordDto(String password, String confirmPassword) {
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
