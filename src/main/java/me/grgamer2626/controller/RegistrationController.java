package me.grgamer2626.controller;

import jakarta.validation.Valid;
import me.grgamer2626.service.users.UserRegistrationService;
import me.grgamer2626.utils.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	private final UserRegistrationService userService;
	
	@Autowired
	public RegistrationController(UserRegistrationService userService) {
		this.userService = userService;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping
	public String registration(Model model) {
		model.addAttribute("userRegistrationDto", new UserRegistrationDto());
		return "registration";
	}
	
	@PostMapping
	public String registerUser(@ModelAttribute("userRegistrationDto") @Valid UserRegistrationDto dto, BindingResult bindingResult) {
		userService.validateUser(dto, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return "registration";
		}
		
		userService.registerUser(dto);
		return "redirect:/";
	}
}
