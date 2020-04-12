package edu.gmu.ludo.entity;

import java.security.SecureRandom;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "user")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_ROLE = "ROLE_PLAYER";
	public static final int MAX_FAILED_TRY = 5;

	// SWE_681 user id is his/her username. This prevent creating an account
	// with a username that already exists.
	@Id
	@Column(name = "username")
	private String username;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "avatar", nullable = true)
	private Integer avatar;

	@Column(name = "salt", nullable = false)
	private Long salt;

	// SWE_681 logging failed attempts of users of blocking malicious behavior
	@Column(name = "failed_attempt")
	private Integer failedAttempt;

	@Column(name = "blocked")
	private boolean blocked;

	@Transient
	private String confirmedPassword;

	public User() {
	}

	public void setUser(User newUser) {
		this.name = newUser.name;
		this.password = newUser.password;
		this.confirmedPassword = newUser.confirmedPassword;
		this.avatar = newUser.avatar;
		this.failedAttempt = 0;
	}

	public String getName() {
		return name;
	}

	public Integer getAvatar() {
		return avatar;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAvatar(Integer avatar) {
		this.avatar = avatar;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public void setSalt(Long salt) {
		this.salt = salt;
	}

	public Long getSalt() {
		return salt;
	}

	public Integer getFailedAttempt() {
		return failedAttempt;
	}

	public void setFailedAttempt(Integer failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	public void addFailedAttempt() {
		this.failedAttempt = failedAttempt + 1;
		if (this.failedAttempt > MAX_FAILED_TRY)
			this.blocked = true;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		User user = (User) o;

		return username.equals(user.username);
	}

	@Override
	public int hashCode() {
		return 13 * username.hashCode();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(DEFAULT_ROLE);
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void encodePassword(PasswordEncoder passwordEncoder) {
		// SWE_681 salt value, which is used as salt, is generated by PRNG
		this.salt = new SecureRandom().nextLong();
		// SWE_681 hashing password, with the "salt" field field as salt.
		this.password = passwordEncoder.encode(password);
	}

}
