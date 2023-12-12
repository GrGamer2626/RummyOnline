package me.grgamer2626.controller;

import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.UserRepository;
import me.grgamer2626.service.lobby.LobbyService;
import me.grgamer2626.utils.memoryRepository.exceptions.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LobbyController {
	
	private LobbyService lobbyService;
	private UserRepository userRepository;
	
	
	@Autowired
	public LobbyController(LobbyService lobbyService, UserRepository userRepository) {
		this.lobbyService = lobbyService;
		this.userRepository = userRepository;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/lobby")
	public String lobby(Model model) {
		model.addAttribute("tables", lobbyService.getTables().values());
		
		return "lobby";
	}
	
	@GetMapping("/createTable")
	public String createTable(RedirectAttributes redirectAttributes, Principal principal) {
		User user = userRepository.findByName(principal.getName());
		
		try {
			GameTable table = lobbyService.createTable(user);
			redirectAttributes.addAttribute("tableId", table.getId());
			
			return "redirect:/table/{tableId}";
			
		} catch (RepositoryException e) {
			return "redirect:/lobby";
		}
	}
	
	@GetMapping("/joinTable/{tableId}")
	public String joinTable(@PathVariable long tableId, RedirectAttributes redirectAttributes, Principal principal) {
		GameTable table = lobbyService.getTable(tableId);
		if(table == null) {
			return "redirect:/lobby";
		}
		
		User user = userRepository.findByName(principal.getName());
		table.addUser(user);
		
		redirectAttributes.addAttribute("tableId", tableId);
		
		return "redirect:/table/{tableId}";
	}
}
