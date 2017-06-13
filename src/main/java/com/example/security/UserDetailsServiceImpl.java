package com.example.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dao.UserRepository;
import com.example.domain.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UserRepository userRepository;

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		{

			User user = userRepository.getUserByName(username);

			if (user == null) {
				throw new UsernameNotFoundException(String.format("User with the username %s doesn't exist", username));
			}

			return user;
		}

	}
}
