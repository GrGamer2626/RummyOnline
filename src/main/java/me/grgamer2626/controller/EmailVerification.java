package me.grgamer2626.controller;


import jakarta.servlet.http.HttpServletRequest;
import me.grgamer2626.event.RegistrationCompleteEvent;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.token.VerificationToken;
import me.grgamer2626.service.token.VerificationTokenService;
import me.grgamer2626.service.users.UserService;
import me.grgamer2626.service.users.exceptions.emailValidation.EmailAlreadyConfirmedException;
import me.grgamer2626.service.users.exceptions.emailValidation.EmailVerificationException;
import me.grgamer2626.service.users.exceptions.emailValidation.TokenExpiredException;
import me.grgamer2626.service.users.registration.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/email-verification")
public class EmailVerification {
	
	private final UserService userService;
	private final UserRegistrationService registrationService;
	private final VerificationTokenService tokenService;
	private final ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public EmailVerification(UserService userService, UserRegistrationService registrationService, VerificationTokenService tokenService, ApplicationEventPublisher eventPublisher) {
		this.userService = userService;
		this.registrationService = registrationService;
		this.tokenService = tokenService;
		this.eventPublisher = eventPublisher;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/error")
	public String emailVerificationError() {
		return "validation-error";
	}
	
	@GetMapping("/token-expired")
	public String registrationError(@RequestParam("userId") Long userId, Model model) {
		model.addAttribute("userId", userId);
		
		return "validation-token-expired";
	}
	
	@GetMapping("/success")
	public String emailValidationSuccess() {
		return "validation-success";
	}
	
	@GetMapping("/resend-email")
	public String resendVerificationEmail(@RequestParam("userId") Long userId, HttpServletRequest request) {
		User user = userService.findById(userId);
		if(user == null || user.isEnabled()) return "redirect:/";
		
		VerificationToken token = tokenService.findByUser(user);
		if(token == null) return "redirect:/email-verification/error";
		if(!token.isExpired()) return "redirect:/";
		tokenService.deleteVerificationToken(token);
		
		String applicationUrl = registrationService.createApplicationUrl(request);
		eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl));
		
		return "redirect:/email-verification/token-expired?userId=" + userId;
	}
	
	@GetMapping
	public String verifyEmail(@RequestParam("token") String verificationToken)  {
		VerificationToken token = tokenService.findByToken(verificationToken);
		try {
			registrationService.validateEmail(token);
			return "redirect:/email-verification/success";
			
		} catch (EmailAlreadyConfirmedException e) {
			return "redirect:/";
			
		} catch (TokenExpiredException e) {
			User user = token.getUser();
			return "redirect:/email-verification/token-expired?userId=" + user.getId();
			
		} catch (EmailVerificationException e) {
			e.printStackTrace();
			return "redirect:/email-verification/error";
		}
	}
	
	
}
