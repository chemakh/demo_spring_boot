package com.example.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by User on 07/11/2016.
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Column(unique = true, nullable = false)
	@NotNull
	private String name;

	@Pattern(regexp = ".+@.+\\.[a-z]+",message="Invalid Email")
	//@Email (message="Invalid Email")
	private String email;

	@Column(nullable = false)
	@NotNull
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Column(nullable = false)
	@JsonProperty
	private boolean isAdmin;

	public User() {
		super();
	}

	public User(String name, String password, boolean isAdmin, String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
		this.isAdmin = isAdmin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> authorities = new ArrayList<>();
		if (this.isAdmin()) {
			authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_PUBLIC");
		} else
			authorities = AuthorityUtils.createAuthorityList("ROLE_PUBLIC");
		return authorities;

	}

	@Override
	public String getUsername() {
		return this.getName();
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

}
