package com.example.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public User getUserByName(String name);
	
}
