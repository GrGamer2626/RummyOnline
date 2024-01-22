package me.grgamer2626.model.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



public class RummyUserDetails implements UserDetails {
	
	private String userName;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean enabled;
	private boolean locked = false;
	private boolean accountExpired = false;
	private boolean credentialsExpired = false;
	
	
	public RummyUserDetails(User user) {
		userName = user.getName();
		password = user.getPassword();
		enabled = user.isEnabled();
		
		List<String> roles = user.getRoles().stream()
				.map(role -> role.getName().name())
				.collect(Collectors.toList());
		
		authorities = AuthorityUtils.createAuthorityList(roles);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String getUsername() {
		return userName;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getName())
				.append(" [")
				.append("Username=")                .append(userName).append(", ")
				.append("Password=[PROTECTED], ")
				.append("Enabled=")                 .append(enabled).append(", ")
				.append("AccountNonExpired=")       .append(!accountExpired).append(", ")
				.append("credentialsNonExpired=")   .append(!credentialsExpired).append(", ")
				.append("AccountNonLocked=")        .append(!locked).append(", ")
				.append("Granted Authorities=")     .append(authorities)
				.append("]");
		
		return sb.toString();
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
}
