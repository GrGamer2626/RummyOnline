package me.grgamer2626.model.users.roles;

import jakarta.persistence.*;

@Entity
@Table(name = "Rummy_Roles")
public class Role {
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "Name", unique = true)
	private RoleType name;
	
	public Role() {}
	
	public Role(long id, RoleType name) {
		this.id = id;
		this.name = name;
	}
	
	public Role(RoleType name) {
		this.name = name;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public RoleType getName() {
		return name;
	}
	
	public void setName(RoleType name) {
		this.name = name;
	}
}
