package com.example.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dao.UserRepository;
import com.example.domain.User;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserRepository userRepository;

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getUserByName(String name) {
		return this.userRepository.getUserByName(name);
	}

	@Override
	public User addUser(User user) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return this.userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {

		List<User> list = new ArrayList<User>();
		this.userRepository.findAll().iterator().forEachRemaining(list::add);

		Collections.sort(list, new Comparator<User>() {

			@Override
			public int compare(User arg0, User arg1) {
				return arg1.getId().compareTo(arg0.getId());
			}
		});

		return list;
	}

}
