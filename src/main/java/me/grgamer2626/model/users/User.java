package me.grgamer2626.model.users;

import jakarta.persistence.*;
import me.grgamer2626.model.users.roles.Role;

import java.util.Collection;

@Entity
@Table(name = "Rummy_Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	@Column(name = "NickName", unique = true)
	private String name;
	@Column(name = "Email", unique = true)
	private String email;
	@Column(name = "Password")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "Rummy_Users_Roles",
			joinColumns = {@JoinColumn(name="UserId", table = "Rummy_Users") },
			inverseJoinColumns = {@JoinColumn(name="RoleId", table = "Rummy_Roles") }
		)
	private Collection<Role> roles;
	
	public User() {	}
	
	public User(String name, String email, String password, Collection<Role> roles) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	
	public User(long id, String name, String email, String password, Collection<Role> roles) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Collection<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
}
