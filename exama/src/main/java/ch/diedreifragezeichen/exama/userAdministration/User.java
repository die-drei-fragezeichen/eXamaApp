package ch.diedreifragezeichen.exama.userAdministration;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@DynamicUpdate
@Table(name = "users")
public class User {

	@Id
	@Column(name = "user_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_email", unique = true, nullable = false, length = 45)
	private String email;

	@Column(name = "user_password", nullable = false, length = 255)
	private String password;

	@Column(name = "user_firstname", nullable = false, length = 20)
	private String firstName;

	@Column(name = "user_lastname", nullable = false, length = 20)
	private String lastName;

	@Column(name = "user_enabled", nullable = false, length = 1)
	private boolean isEnabled;

	@Column(name = "user_loggedin", nullable = false, length = 1)
	private boolean loggedIn;

	@Column(name = "user_lastlogin", nullable = true)
	@DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
	private LocalDate lastLogin;

	@Column(name = "user_createdon", nullable = false)
	@DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
	private LocalDate createdOn;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "map_users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public LocalDate getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(LocalDate lastLogin) {
		this.lastLogin = lastLogin;
	}

	public LocalDate getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}
}