package me.grgamer2626.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import me.grgamer2626.event.ForgetPasswordEvent;
import me.grgamer2626.model.users.User;
import me.grgamer2626.service.captcha.CaptchaService;
import me.grgamer2626.service.changePassword.ForgetPasswordService;
import me.grgamer2626.service.users.UserService;
import me.grgamer2626.utils.dto.forgetPassword.ForgetPasswordDto;
import me.grgamer2626.utils.dto.captcha.CaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forget-password")
public class ForgetPasswordController {
	
	private final ForgetPasswordService passwordService;
	private final CaptchaService captchaService;
	private final ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public ForgetPasswordController(ForgetPasswordService passwordService, CaptchaService captchaService, ApplicationEventPublisher eventPublisher) {
		this.passwordService = passwordService;
		this.captchaService = captchaService;
		this.eventPublisher = eventPublisher;
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
	
	@PostMapping
	public String sendMail(@ModelAttribute("forgetPasswordDto") @Valid ForgetPasswordDto dto, @RequestParam("h-captcha-response") String hCaptchaResponse, Model model, BindingResult bindingResult, HttpServletRequest request) {
		passwordService.validateDto(dto, bindingResult);
		if(bindingResult.hasErrors()) {
			model.addAttribute("forgetPasswordDto", dto);
			return "forget-password";
		}
		
		CaptchaResponse captchaResponse = captchaService.validateToken(hCaptchaResponse);
		if(captchaResponse.success()) {
			User user = passwordService.findByEmail(dto.getEmail());
			
			String applicationUrl = passwordService.createApplicationUrl(request);
			
			eventPublisher.publishEvent(new ForgetPasswordEvent(user, applicationUrl));
			
			return  "redirect:/forget-password/sent";
		}
		
		
		
		return  "forget-password";
	}
}
