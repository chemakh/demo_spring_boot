package com.example.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.Tags;

public interface TagsRepository extends CrudRepository<Tags, Long> {
	
	
	Optional<Tags> findOneById(long id);

}
