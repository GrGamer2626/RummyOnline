package me.grgamer2626.controller;

import me.grgamer2626.model.games.player.Player;
import me.grgamer2626.model.tables.GameTable;
import me.grgamer2626.model.users.User;
import me.grgamer2626.model.users.UserRepository;
import me.grgamer2626.service.lobby.LobbyService;
import me.grgamer2626.utils.dto.lobby.CreateTableDto;
import me.grgamer2626.utils.dto.lobby.JoinTableDto;
import me.grgamer2626.utils.memoryRepository.exceptions.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

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
	
	@MessageMapping("/lobbySocket/table/create")
	@SendTo("/topic/table/create")
	@SendToUser("/topic/table/create")
	public CreateTableDto createTable(Principal principal) {
		User user = userRepository.findByName(principal.getName());
		try {
			GameTable table = lobbyService.createTable(user);
			
			return new CreateTableDto(table.getId(), table.getTableOwner().getName());
			
		}catch(RepositoryException e) {
			return null;
		}
	}
	
	@MessageMapping("/lobbySocket/table/join")
	@SendToUser("/topic/table/join")
	public JoinTableDto joinTable(@Payload JoinTableDto joinTableDto, Principal principal) {
		long tableId = joinTableDto.tableId();
		GameTable table = lobbyService.getTable(tableId);
		
		if(table == null) return null;
		
		User user = userRepository.findByName(principal.getName());
		table.addUser(user);
		
		return joinTableDto;
	}
	
	@GetMapping("/leaveTable/{tableId}")
	public String leaveTable(@PathVariable long tableId, Principal principal) {
		GameTable table = lobbyService.getTable(tableId);
		
		User user = userRepository.findByName(principal.getName());
		table.removeUser(user);
		
		if(table.getTableOwner().equals(user) && !table.isEmpty()) setNewTableOwner(table);
		
		
		return "redirect:/lobby";
	}
	
	private void setNewTableOwner(GameTable table) {
		List<Player> playerSlots = table.getPlayerSlots().getNonNull();
		if(!playerSlots.isEmpty()) {
			Player player = playerSlots.get(0);
			
			for(User otherUsers : table.getUsersInTable()) {
				if(otherUsers.getId() != player.getId()) continue;
				
				table.setTableOwner(otherUsers);
				break;
			}
			return;
		}
		List<User> users = table.getUsersInTable();
		table.setTableOwner(users.get(0));
	}
}
