package me.grgamer2626.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@ModelAttribute
	public void addGlobalAttributes(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		boolean logged = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
		model.addAttribute("logged", logged);
		
		if(logged) {
			String username = auth.getName();
			model.addAttribute("username", username);
		}
		
	}
	
}
