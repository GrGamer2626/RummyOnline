package me.grgamer2626.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import me.grgamer2626.event.RegistrationCompleteEvent;
import me.grgamer2626.model.users.User;
import me.grgamer2626.service.users.registration.UserRegistrationService;
import me.grgamer2626.service.users.exceptions.registration.RegistrationException;
import me.grgamer2626.utils.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	private final UserRegistrationService userService;
	private final ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public RegistrationController(UserRegistrationService userService, ApplicationEventPublisher eventPublisher) {
		this.userService = userService;
		this.eventPublisher = eventPublisher;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping
	public String registration(Model model) {
		Object dto = model.getAttribute("userRegistrationDto");
		if(dto != null) {
			UserRegistrationDto registrationDto = (UserRegistrationDto) dto;
			registrationDto.setPassword("");
			registrationDto.setConfirmPassword("");
			
		}else {
			model.addAttribute("userRegistrationDto", new UserRegistrationDto());
			
		}
		return "registration";
	}
	
	@GetMapping("/success")
	public String registrationSuccess() {
		return "registration-success";
	}
	
	@PostMapping
	public String registerUser(@ModelAttribute("userRegistrationDto") @Valid UserRegistrationDto dto, BindingResult bindingResult, Model model, HttpServletRequest request) {
		userService.validateUser(dto, bindingResult);
		if(bindingResult.hasErrors()) {
			model.addAttribute("userRegistrationDto", dto);
			return "registration";
		}
		try {
			User user = userService.registerUser(dto);
			
			String applicationUrl = userService.createApplicationUrl(request);
			
			eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl));
			
			model.addAttribute("userId", user.getId());
			
			return "redirect:/registration/success";
			
		} catch (RegistrationException e) {
			model.addAttribute("userRegistrationDto", dto);
			e.printStackTrace();
			return "registration";
			//throw new RuntimeException(e);
		}
	}
	
}
