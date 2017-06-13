package com.example.service;

import java.util.List;

import com.example.domain.User;

public interface UserService {

	public User getUserByName(String Name);
	
	public List<User> getAllUsers();

	public User addUser(User user);

}
