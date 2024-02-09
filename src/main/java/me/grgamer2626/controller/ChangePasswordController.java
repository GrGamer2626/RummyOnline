package me.grgamer2626.controller;

import jakarta.validation.Valid;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.changePassword.ChangePassword;
import me.grgamer2626.service.captcha.CaptchaService;
import me.grgamer2626.service.changePassword.ChangePasswordService;
import me.grgamer2626.service.users.UserService;
import me.grgamer2626.utils.dto.captcha.CaptchaResponse;
import me.grgamer2626.utils.dto.forgetPassword.ChangePasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/change-password")
public class ChangePasswordController {
	
	private final UserService userService;
	private final ChangePasswordService passwordService;
	private final CaptchaService captchaService;
	
	@Autowired
	public ChangePasswordController(UserService userService, ChangePasswordService passwordService, CaptchaService captchaService) {
		this.userService = userService;
		this.passwordService = passwordService;
		this.captchaService = captchaService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping
	public String changePassword(Model model) {
		Object dto = model.getAttribute("changePasswordDto");
		if(dto != null) {
			ChangePasswordDto forgetPasswordDto = (ChangePasswordDto) dto;
			forgetPasswordDto.setPassword("");
			forgetPasswordDto.setConfirmPassword("");
			
		}else {
			model.addAttribute("changePasswordDto", new ChangePasswordDto());
			
		}
		model.addAttribute("hcaptchaSiteKey", captchaService.getSiteKey());
		
		return "change-password";
	}
	
	@PostMapping
	public String changePassword(@ModelAttribute("changePasswordDto") @Valid ChangePasswordDto dto, @RequestParam("token") String token, @RequestParam("h-captcha-response") String hCaptchaResponse, Model model, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("changePasswordDto", dto);
			return "change-password";
		}
		
		CaptchaResponse captchaResponse = captchaService.validateToken(hCaptchaResponse);
		if(captchaResponse.success()) {
			ChangePassword changePassword = passwordService.findByToken(token);
			if(changePassword == null) return "redirect:/change-password/error";
			
			User user = changePassword.getUser();
			
			passwordService.changePassword(user ,dto.getPassword());
		}
		
		return "";
	}
}
