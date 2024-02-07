package me.grgamer2626.controller;


import jakarta.validation.Valid;
import me.grgamer2626.service.captcha.CaptchaService;
import me.grgamer2626.service.users.UserService;
import me.grgamer2626.utils.dto.ForgetPasswordDto;
import me.grgamer2626.utils.dto.captcha.CaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forget-password")
public class ForgetPasswordController {
	
	private final UserService userService;
	private final CaptchaService captchaService;
	
	@Autowired
	public ForgetPasswordController(UserService userService, CaptchaService captchaService) {
		this.userService = userService;
		this.captchaService = captchaService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping
	public String forgetPassword(Model model) {
		Object dto = model.getAttribute("forgetPasswordDto");
		if(dto != null) {
			ForgetPasswordDto forgetPasswordDto = (ForgetPasswordDto) dto;
			forgetPasswordDto.setEmail("");
			
		}else {
			model.addAttribute("forgetPasswordDto", new ForgetPasswordDto());
		}
		model.addAttribute("hcaptchaSiteKey", captchaService.getSiteKey());
		
		return "forget-password";
	}
	
	@GetMapping("/sent")
	public String forgetPasswordSuccess() {
		
		return "forget-password-sent";
	}
	
	
	@GetMapping("/change-password")
	public String resetPassword(Model model) {
		model.addAttribute("hcaptchaSiteKey", captchaService.getSiteKey());
		
		return "change-password";
	}
	@PostMapping
	public String sendMail(@ModelAttribute("forgetPasswordDto") @Valid ForgetPasswordDto dto, @RequestParam("h-captcha-response") String hCaptchaResponse, Model model, BindingResult bindingResult) {
		if(!userService.isEmailRegistered(dto.getEmail())) {
			bindingResult.rejectValue("email", "error.emailNotExist", "Provide email has been never registered!");
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("forgetPasswordDto", dto);
			return "forget-password";
		}
		//captcha
		CaptchaResponse captchaResponse = captchaService.validateToken(hCaptchaResponse);
		if(captchaResponse.success()) {
			//Send mail with link to /forget-password/change-password?token=${token}
			//Redirect to mail send
		}
		
		
		return  "redirect:/forget-password/sent";
		
	}

	
}
