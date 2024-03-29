package me.grgamer2626.model.users.token;

import jakarta.persistence.*;
import me.grgamer2626.model.users.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Entity
@Table(name = "Rummy_Tokens")
public class VerificationToken {
	
	private static final int EXPIRATION_TIME = 10;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	@OneToOne
	@JoinColumn(name = "User_Id")
	private User user;
	@Column(name = "Token")
	private String token;
	@Column(name = "ExpirationTime")
	private Date expirationTime;
	
	public VerificationToken() {}
	
	public VerificationToken(long id, String token, Date expirationTime, User user) {
		this.id = id;
		this.token = token;
		this.expirationTime = expirationTime;
		this.user = user;
	}
	
	public VerificationToken(User user, String token) {
		this.user = user;
		this.token = token;
		this.expirationTime = getTokenExpirationTime();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public Date getExpirationTime() {
		return expirationTime;
	}
	
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	private Date getTokenExpirationTime() {
		Instant currentInstant = Instant.now();
		Instant expirationInstant = currentInstant.plus(EXPIRATION_TIME, ChronoUnit.MINUTES);
		
		return Date.from(expirationInstant);
	}
	
	public boolean isExpired() {
		Date now = Date.from(Instant.now());
		return now.after(expirationTime);
	}
	
}
